import csv

file_name = 'Business_Locations.csv'

csvReader = csv.reader(open(file_name,"rb"),delimiter=',')
data = list(csvReader)
data[0].append("longitude")
data[0].append("latitude")

#values = re.search(r"\[(\*?)\]", data[1][-1])
for i in range(1,len(data)):
    location = data[i][-1]
    xy = location[location.index('(')+1:location.index(')')].split(',') if ('(' in location and ')' in location) else ["",""]
    data[i] += xy

with open('Business_Locations_1.0.csv', 'wb') as file:
    writer = csv.writer(file, delimiter=',')
    writer.writerows(data)




