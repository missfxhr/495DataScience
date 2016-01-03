import csv,re

file_name = 'incidents.csv'

csvReader = csv.reader(open(file_name,"rb"),delimiter=',')
initial_rows = list(csvReader)

header_row = initial_rows.pop(0)
data_rows = initial_rows

# header_row.append("latitude")
# header_row.append("longitude")
header_row = ["incident_date" if val == "Date" else "incident_time" if val == "Time" else val for val in header_row]

# for i in range(len(data_rows)):
#     location = data_rows[i][-1]
#     group = re.search("(\d+.\d+,\s*-\d+.\d+)",location)
#     if group == None:
#         xy = ["",""]
#         # xy = location[length-(location[::-1].index('(')):length-1-location[::-1].index(')')].split(',')
#     else:
#         xy = group.group().split(',')
#     data_rows[i] += xy

index_needed = [0,1,2,3,4,5,6,7,8,9,10,12]
new_header_row = [val for index,val in enumerate(header_row) if index in index_needed]
# data_rows = [[val for index,val in enumerate(row) if index in index_needed] for row in data_rows if row[5].lower() == "san francisco" and row[6].lower()=="ca"]

new_data_rows = []
for i in range(len(data_rows)):
    year = int(data_rows[i][4].split('/')[2])
    if year >= 2010 and year <= 2014:
        new_row = []
        for j in index_needed:
            data = data_rows[i][j] if not data_rows[i][j] in ["","n.a."] else "null"
            # print i,j,data
            new_row.append(data)
        new_data_rows.append(new_row)

new_data = [new_header_row] + new_data_rows

with open('Incidents_2010-2014_final.csv', 'wb') as file:
    writer = csv.writer(file, delimiter=',')
    writer.writerows(new_data)




