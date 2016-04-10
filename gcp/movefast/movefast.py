#!/usr/bin/env python
"""movefast.py sets up the infrastructure for a fictional company MoveFast.

The infrastructure makes heavy use of Google Cloud Platform.

You need to get the json credentials file before using this script. See
https://developers.google.com/identity/protocols/application-default-credentials#howtheywork.

After getting the credentials, you can simply run this script by:

    movefast.py --google-json-credentials <your-gcp-json-credentials-file>


    Copyright 2016 Heung Ming Tai

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
"""
from __future__ import print_function
from __future__ import absolute_import
from __future__ import division
from __future__ import unicode_literals

import argparse
import json
import logging
import os
import pprint
import sys

from apiclient import discovery
from oauth2client import client as oauth2client
import googleapiclient
import requests
import six
import time

from sorno import loggingutil


_log = logging.getLogger()
_plain_error_logger = None  # will be created in main()


class MoveFast(object):
    """A console application to do work"""
    def __init__(self, args):
        """
        Args:
            args (argparse.Namespace): The flags for the script.
        """
        self.args = args
        self.credentials = None
        self.compute = None
        self.project_id = None
        self.existing_instances = {}
        self.zone_name = args.zone

    def run(self):
        """The entry point of setting up MoveFast."""
        retcode = self._process_args()
        if retcode != 0:
            return retcode

        self._create_credentials()
        self._create_compute_service_client()

        # create all GCP resources
        self._setup_gce()

        return 0

    def _process_args(self):
        if self.args.google_json_credentials:
            os.environ["GOOGLE_APPLICATION_CREDENTIALS"] = (
                self.args.google_json_credentials
            )

        project_id = self._get_project_id()

        if not project_id:
            _plain_error_logger.info(
                "Please provide project id with --project-id"
            )
            return 1

        self.project_id = project_id
        return 0

    def _create_credentials(self):
        """Creates credentials to use GCP API

        https://cloud.google.com/compute/docs/tutorials/python-guide
        """
        self.credentials = oauth2client.GoogleCredentials.get_application_default()

    def _create_compute_service_client(self):
        """Creates the client to use the Google Compute API.

        https://cloud.google.com/compute/docs/tutorials/python-guide
        """
        self.compute = discovery.build(
            "compute",
            "v1",
            credentials=self.credentials,
        )

    def _get_project_id(self):
        project_id = self.args.project_id
        if not project_id:
            # try to get it from credentials
            credentials_filename = oauth2client._get_environment_variable_file()
            if not credentials_filename:
                credentials_filename = oauth2client._get_well_known_file()

            if credentials_filename:
                with open(credentials_filename) as file_obj:
                    client_credentials = json.load(file_obj)
                    project_id = client_credentials['project_id']
        return project_id

    def _setup_gce(self):
        indent = " " * 2
        print("Existing Google Compute Engine instances:")
        resp = self.compute.instances().aggregatedList(
            project=self.project_id).execute()
        logging.debug("Resp: %s", resp)
        is_first = True
        for key, obj in resp['items'].iteritems():
            instances = obj.get('instances')
            if not instances:
                continue

            if is_first:
                is_first = False
            else:
                _print_horizontal_line()

            print("%s:" % key)
            for instance in instances:
                self.existing_instances[instance['name']] = instance
                print(indent + instance['name'])
                external_ip = self._get_instance_external_ip(instance)
                if external_ip:
                    print(
                        indent * 2 + "External IP - %s" % (
                            external_ip,
                        )
                    )

        _print_section_separator()
        print("Existing zones:")

        resp = self.compute.zones().list(project=self.project_id).execute()
        logging.debug("Resp: %s", resp)
        for zone in resp['items']:
            zone_desc = zone['description']
            print(zone_desc)
            if not self.zone_name and zone_desc.startswith("us-central1"):
                self.zone_name = zone_desc

        if self.args.do_sample_gce_vm_setup:
            _print_section_separator()
            _log.info("Set up Google Compute Engine resources")

            self._setup_echo_service()

    def _setup_echo_service(self):
        _log.info(
            "Create a Google Compute Engine vm with a simple echo serice"
            " that you can find in:"
            " https://github.com/hermantai/samples/blob/master/python/cherrypy_web_server.py"
        )

        instance_name = self.args.resource_name_prefix + "echo-service"
        if instance_name in self.existing_instances:
            print(
                "Instance %s already exists,"
                " skip creating an echo service" % instance_name
            )
            echo_service_instance = self.existing_instances[instance_name]
        else:
            echo_service_instance = self._create_echo_service_on_gce(
                instance_name,
            )

        _log.info("Test the echo service...")
        external_ip = self._get_instance_external_ip(echo_service_instance)
        if not external_ip:
            _log.error("No external ip")
            return

        url = "http://%s/echo?se=hello" % external_ip
        _log.info("Connect to %s", url)
        # check for at most two minutes
        deadline = time.time() + 120
        while True:
            try:
                resp = requests.get(url)
                break
            except requests.exceptions.ConnectionError as e:
                diff = deadline - time.time()
                if diff > 0:
                    _log.error("Cannot connect to the echo service: %s", e)
                    _log.info(
                        "%d seconds before deadline, sleep 5 seconds then retry...",
                        diff,
                    )
                    time.sleep(5)
                    continue
                else:
                    _log.exception("Failed to connect to the echo service")
                    return

        _log.debug("Resp: %s", resp.__dict__)
        if resp.status_code == 200:
            _log.info("Succeeded!")
        else:
            _log.error("Failed: %s", resp.__dict__)


    def _create_echo_service_on_gce(self, instance_name):
        instance_config = self._get_echo_service_instance_config(
            self.zone_name,
            instance_name,
        )
        _log.debug("Instance config: %s", instance_config)

        operation = self.compute.instances().insert(
            project=self.project_id,
            zone=self.zone_name,
            body=instance_config).execute()
        _log.debug("Resp: %s", operation)

        self._wait_for_operation(self.zone_name, operation)
        _log.info(
            "You should be able to see the startup script"
            " in /var/run/google.startup.script"
        )
        _log.info(
            "The output of the startup script run at startup is in"
            " /var/log/daemon.log"
        )

        instance = self.compute.instances().get(
            project=self.project_id,
            zone=self.zone_name,
            instance=instance_name,
        ).execute()
        if instance:
            self.existing_instances[instance_name] = instance

        return instance

    def _get_echo_service_instance_config(self, zone, name):
        """https://cloud.google.com/compute/docs/tutorials/python-guide"""

        source_disk_image = \
            "https://www.googleapis.com/compute/v1/projects/debian-cloud/global/images/debian-8-jessie-v20160329"
        machine_type = "zones/%s/machineTypes/f1-micro" % zone
        startup_script = """
            echo Hello && date +%Y-%m-%dT%H:%M:%S
            fruit=$(curl http://metadata/computeMetadata/v1/instance/attributes/fruit -H "Metadata-Flavor: Google")
            echo My favorite food is "$fruit".
            sudo apt-get install -y python-pip
            sudo apt-get install -y git
            sudo pip install cherrypy

            mkdir /tmp/apps
            cd /tmp/apps
            git clone https://github.com/hermantai/samples.git
            sudo python samples/python/cherrypy_web_server.py --port 80 --host 0.0.0.0
        """

        config = {
            'name': name,
            'machineType': machine_type,

            # Specify the boot disk and the image to use as a source.
            'disks': [
                {
                    "type": "PERSISTENT",
                    'boot': True,
                    'model': "READ_WRITE",
                    'autoDelete': True,
                    'initializeParams': {
                        'sourceImage': source_disk_image,
                        'diskType': "projects/%s/zones/%s/diskTypes/pd-ssd" % (
                            self.project_id, zone),
                        'diskSizeGb': "10"
                    }
                }
            ],

            # Specify a network interface with NAT to access the public
            # internet.
            'networkInterfaces': [{
                'network': 'global/networks/default',
                'accessConfigs': [
                    {'type': 'ONE_TO_ONE_NAT', 'name': 'External NAT'}
                ]
            }],

            # Allow the instance to access cloud storage and logging.
            'serviceAccounts': [{
                'email': 'default',
                'scopes': [
                    # every cloud API
                    "https://www.googleapis.com/auth/cloud-platform",
                ]
            }],
            'scheduling': {
                "preemptible": False,
                "onHostMaintenance": "MIGRATE",
                "automaticRestart": True
            },

            "tags": {
                "items": [
                    "http-server",
                    "https-server",
                ]
            },

            # Metadata is readable from the instance and allows you to
            # pass configuration from deployment scripts to instances.
            'metadata': {
                'items': [
                    {
                        # Startup script is automatically executed by the
                        # instance upon startup.
                        'key': 'startup-script',
                        'value': startup_script
                    }, {
                        'key': 'fruit',
                        'value': "blueberries",
                    },
                ]
            }
        }
        return config

    def _wait_for_operation(self, zone_name, operation):
        while True:
            _log.info(
                "Waiting for operation %s to finish (status: %s)...",
                operation['name'],
                operation['status'],
            )
            result = self.compute.zoneOperations().get(
                project=self.project_id,
                zone=zone_name,
                operation=operation['name']).execute()

            _log.debug("Resp: %s", result)

            if result['status'] == 'DONE':
                _log.info("Done")
                if 'error' in result:
                    raise Exception(result['error'])
                return result

            time.sleep(3)

    def _get_instance_external_ip(self, instance):
        network_access_configs = _get_null_safe(
            instance,
            'networkInterfaces',
            'accessConfigs',
        )
        if network_access_configs:
            for network_access_config in network_access_configs:
                if network_access_config['name'] == "External NAT":
                    return network_access_config.get('natIP')

        return None


def _print_horizontal_line():
    print("-" * 70)


def _print_section_separator():
    print("=" * 70)


def _get_null_safe(mixed, *keys):
    val = mixed
    for key in keys:
        if val is None:
            return val

        if type(val) is dict:
            val = val.get(key)
        elif type(val) is list:
            if type(key) in six.integer_types:
                if key < len(val):
                    val = val[key]
                else:
                    val = None
            else:
                # iterate the list to find the first item
                # that has the specified key, and use that as the value
                new_val = None
                for item in val:
                    new_val = _get_null_safe(item, key)
                    if new_val is not None:
                        break
                val = new_val

    return val


def parse_args(cmd_args):
    description = __doc__.split("Copyright 2016")[0].strip()

    parser = argparse.ArgumentParser(
        description=description,
        formatter_class=argparse.RawDescriptionHelpFormatter,
    )
    parser.add_argument(
        "--debug",
        action="store_true",
    )
    parser.add_argument(
        "--google-json-credentials",
        help="Path to the google json credentials file. See https://developers.google.com/identity/protocols/application-default-credentials#howtheywork",
    )
    parser.add_argument(
        "--project-id",
    )
    parser.add_argument(
        "--resource-name-prefix",
        default="movefast-",
    )
    parser.add_argument(
        "--zone",
    )
    parser.add_argument(
        "--skip-sample-gce-vm-setup",
        action="store_false",
        default=True,
        dest="do_sample_gce_vm_setup",
    )

    args = parser.parse_args(cmd_args)
    return args


def main():
    global _plain_error_logger
    args = parse_args(sys.argv[1:])

    loggingutil.setup_logger(_log, debug=args.debug)
    if not args.debug:
        # tone down the verbosity of some modules
        verbose_modules = [
            "googleapiclient.discovery",
            "oauth2client.client",
            "requests",
        ]
        for mod in verbose_modules:
            logging.getLogger(mod).setLevel(
                logging.WARNING
            )

    _plain_error_logger = loggingutil.create_plain_logger(
        "PLAIN_ERROR",
        debug=args.debug,
        stdout=False,
    )

    app = MoveFast(args)
    sys.exit(app.run())


if __name__ == '__main__':
    main()
