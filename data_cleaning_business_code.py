import csv

file_name = 'Business_Code.csv'

csvReader = csv.reader(open(file_name,"rb"),delimiter=',')
initial_rows = list(csvReader)

header_row = initial_rows.pop(0)
data_rows = initial_rows

index_needed = [0,1,2,3]
new_header_row = [val for index,val in enumerate(header_row) if index in index_needed]
# data_rows = [[val for index,val in enumerate(row) if index in index_needed] for row in data_rows if row[5].lower() == "san francisco" and row[6].lower()=="ca"]

new_data_rows = []
for i in range(len(data_rows)):
    if True:
        new_row = []
        for j in index_needed:
            data = data_rows[i][j] if not data_rows[i][j] in ["","n.a."] else "null"
            # print i,j,data
            new_row.append(data)
        new_data_rows.append(new_row)

new_data = [new_header_row] + new_data_rows

with open('Business_Code_final.csv', 'wb') as file:
    writer = csv.writer(file, delimiter=',')
    writer.writerows(new_data)




