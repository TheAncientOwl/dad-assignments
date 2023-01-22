def make_array_file(fileName, elementsCount, value):
  f = open(fileName, "w")

  f.write(str(elementsCount))
  f.write('\n')

  for i in range(0, elementsCount):
    f.write(str(value))
    f.write('\n')

  f.close()

make_array_file("arr1.txt", 100, 2.2)
make_array_file("arr2.txt", 100, 3.3)
