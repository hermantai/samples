package com.gmail.htaihm.playandroidkeystoresystem;

import android.content.DialogInterface;
import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * A demo for using Android Keystore from:
 * https://www.androidauthority.com/use-android-keystore-store-passwords-sensitive-information-623779/
 *
 * Source: https://github.com/obaro/SimpleKeystoreApp
 */
public class MainActivity extends AppCompatActivity {

  private static final String TAG = MainActivity.class.getName();

  private EditText etKeyAlias;
  private EditText etData;
  private EditText etEncryptedData;
  private EditText etDecryptedData;
  private Button btnGenerate;

  private KeyStore keyStore;

  private final List<String> existingKeys = new ArrayList<>();
  private KeysRecyclerViewAdapter keysRecyclerViewAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    etKeyAlias = findViewById(R.id.etKeyAlias);
    btnGenerate = findViewById(R.id.btnGenerate);
    btnGenerate.setOnClickListener(
        view -> createNewKeys(view)
    );
    etData = findViewById(R.id.etData);
    etEncryptedData = findViewById(R.id.etEncryptedData);
    etDecryptedData = findViewById(R.id.etDecryptedData);

    try {
      keyStore = KeyStore.getInstance("AndroidKeyStore");
      keyStore.load(null);
    } catch (KeyStoreException e) {
      e.printStackTrace();
      return;
    } catch (CertificateException e) {
      e.printStackTrace();
    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    keysRecyclerViewAdapter = new KeysRecyclerViewAdapter();
    RecyclerView rvExistingKeys = findViewById(R.id.rvExistingKeys);
    rvExistingKeys.setAdapter(keysRecyclerViewAdapter);
    rvExistingKeys.setLayoutManager(new LinearLayoutManager(this));

    refreshKeys();
  }

  private void refreshKeys() {
    try {
      existingKeys.clear();
      Enumeration<String> aliases = keyStore.aliases();
      while (aliases.hasMoreElements()) {
        existingKeys.add(aliases.nextElement());
      }
    } catch(Exception e) {}

    if(keysRecyclerViewAdapter != null)
      keysRecyclerViewAdapter.notifyDataSetChanged();
  }

  public void createNewKeys(View view) {
    String alias = etKeyAlias.getText().toString();
    try {
      // Create new key if needed
      if (!keyStore.containsAlias(alias)) {
        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 1);
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
            .setAlias(alias)
            .setSubject(new X500Principal("CN=PlayAndroidKeystoreSystem, O=Start With Nah"))
            .setSerialNumber(BigInteger.ONE)
            .setStartDate(start.getTime())
            .setEndDate(end.getTime())
            .build();
        KeyPairGenerator generator = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
        generator.initialize(spec);

        KeyPair keyPair = generator.generateKeyPair();
      }
    } catch (Exception e) {
      Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
      Log.e(TAG, Log.getStackTraceString(e));
    }
    refreshKeys();
  }

  public void deleteKey(final String alias) {
    AlertDialog alertDialog =new AlertDialog.Builder(this)
        .setTitle("Delete Key")
        .setMessage("Do you want to delete the key \"" + alias + "\" from the keystore?")
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            try {
              keyStore.deleteEntry(alias);
              refreshKeys();
            } catch (KeyStoreException e) {
              Toast.makeText(MainActivity.this,
                  "Exception " + e.getMessage() + " occured",
                  Toast.LENGTH_LONG).show();
              Log.e(TAG, Log.getStackTraceString(e));
            }
            dialog.dismiss();
          }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
          }
        })
        .create();
    alertDialog.show();
  }

  public void encryptString(String alias) {
    try {
      KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
      PublicKey publicKey = privateKeyEntry.getCertificate().getPublicKey();

      // Encrypt the text
      String initialText = etData.getText().toString();
      if(initialText.isEmpty()) {
        Toast.makeText(this, "Enter text in the 'Initial Text' widget", Toast.LENGTH_LONG).show();
        return;
      }

      Cipher input = getCipher();
      input.init(Cipher.ENCRYPT_MODE, publicKey);

      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      CipherOutputStream cipherOutputStream = new CipherOutputStream(
          outputStream, input);
      cipherOutputStream.write(initialText.getBytes("UTF-8"));
      cipherOutputStream.close();

      byte [] vals = outputStream.toByteArray();
      etEncryptedData.setText(Base64.encodeToString(vals, Base64.DEFAULT));
    } catch (Exception e) {
      Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
      Log.e(TAG, Log.getStackTraceString(e));
    }
  }

  public void decryptString(String alias) {
    try {
      KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry)keyStore.getEntry(alias, null);
      PrivateKey privateKey = privateKeyEntry.getPrivateKey();

      Cipher output = getCipher();
      output.init(Cipher.DECRYPT_MODE, privateKey);

      String cipherText = etEncryptedData.getText().toString();
      CipherInputStream cipherInputStream = new CipherInputStream(
          new ByteArrayInputStream(Base64.decode(cipherText, Base64.DEFAULT)), output);
      ArrayList<Byte> values = new ArrayList<>();
      int nextByte;
      while ((nextByte = cipherInputStream.read()) != -1) {
        values.add((byte)nextByte);
      }

      byte[] bytes = new byte[values.size()];
      for(int i = 0; i < bytes.length; i++) {
        bytes[i] = values.get(i).byteValue();
      }

      String finalText = new String(bytes, 0, bytes.length, "UTF-8");
      etDecryptedData.setText(finalText);

    } catch (Exception e) {
      Toast.makeText(this, "Exception " + e.getMessage() + " occured", Toast.LENGTH_LONG).show();
      Log.e(TAG, Log.getStackTraceString(e));
    }
  }

  private Cipher getCipher() {
    try {
      if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { // below android m
        return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidOpenSSL"); // error in android 6: InvalidKeyException: Need RSA private or public key
      }
      else { // android m and above
        return Cipher.getInstance("RSA/ECB/PKCS1Padding", "AndroidKeyStoreBCWorkaround"); // error in android 5: NoSuchProviderException: Provider not available: AndroidKeyStoreBCWorkaround
      }
    } catch(Exception exception) {
      throw new RuntimeException("getCipher: Failed to get an instance of Cipher", exception);
    }
  }

  private class KeysRecyclerViewAdapter extends RecyclerView.Adapter<KeyEntryViewHolder> {

    @NonNull
    @Override
    public KeyEntryViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
      LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
      return new KeyEntryViewHolder(layoutInflater.inflate(R.layout.key_list_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull KeyEntryViewHolder keyEntryViewHolder, int i) {
      String key = existingKeys.get(i);
      keyEntryViewHolder.tvKeyAlias.setText(key);
      keyEntryViewHolder.btnDeleteKey.setOnClickListener(
          view -> deleteKey(key)
      );
      keyEntryViewHolder.btnDecryptData.setOnClickListener(
          view -> decryptString(key)
      );
      keyEntryViewHolder.btnEncryptData.setOnClickListener(
          view -> encryptString(key)
      );
    }

    @Override
    public int getItemCount() {
      return existingKeys.size();
    }
  }

  private static class KeyEntryViewHolder extends RecyclerView.ViewHolder {

    private TextView tvKeyAlias;
    private Button btnDeleteKey;
    private Button btnEncryptData;
    private Button btnDecryptData;

    public KeyEntryViewHolder(@NonNull View itemView) {
      super(itemView);

      tvKeyAlias = itemView.findViewById(R.id.tvKeyAlias);
      btnDeleteKey = itemView.findViewById(R.id.btnDeleteKey);
      btnEncryptData = itemView.findViewById(R.id.btnEncryptData);
      btnDecryptData = itemView.findViewById(R.id.btnDecryptData);
    }
  }
}
