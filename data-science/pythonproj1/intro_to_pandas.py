# Tutorials linked from Google.
# https://colab.sandbox.google.com/notebooks/mlcc/intro_to_pandas.ipynb

from __future__ import print_function

import pandas as pd

pd.__version__

city_names = pd.Series(['San Francisco', 'San Jose', 'Sacramento'])
population = pd.Series([852469, 1015785, 485199])

cities = pd.DataFrame({ 'City name': city_names, 'Population': population })

california_housing_dataframe = pd.read_csv("https://download.mlcc.google.com/mledu-datasets/california_housing_train.csv", sep=",")
california_housing_dataframe.describe()

# display first few records
california_housing_dataframe.head()

ax = california_housing_dataframe.hist('housing_median_age')

fig = ax[0][0].get_figure()
fig.savefig('housing_hist.pdf')

print(type(cities['City name']))
# print a series
print(cities['City name'])

print(type(cities['City name'][1]))
cities['City name'][1]

# print first two columns as a table
print(type(cities[0:2]))
cities[0:2]

print("manipulate data: population / 1000, returns a table of data being changed")
print(population / 1000)

print("use np:")
import numpy as np
print(np.log(population))

print("more complex manipulation can be done by apply")
print(population.apply(lambda val: val > 1000000))

print("modifying DataFrames")
cities['Area square miles'] = pd.Series([46.87, 176.53, 97.92])
cities['Population density'] = cities['Population'] / cities['Area square miles']
print(cities)

print("manually reorder the rows")
cities.reindex([2, 0, 1])
print(cities)

print("shuffling with numpy")
cities.reindex(np.random.permutation(cities.index))
print(cities)

