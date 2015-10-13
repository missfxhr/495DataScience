import sqlalchemy,pandas

protocol_name = 'postgresql'
username = 'postgres'
password = '123456'
hostname = 'localhost'
database_name = '495_data_science'
file_name = 'Business_Locations_1.0.csv'
table_name = 'business_locations'

engine = sqlalchemy.create_engine(protocol_name+'://'+username+':'+password+'@'+hostname+'/'+database_name)
df = pandas.read_csv(file_name)
df.to_sql(table_name, engine)