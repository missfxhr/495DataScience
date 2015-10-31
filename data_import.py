import sqlalchemy,pandas

protocol_name = 'postgresql'
username = 'postgresql'
password = '12345678'
hostname = 'datascience495.coxgjlcsyyhn.us-east-1.rds.amazonaws.com:5432'
database_name = 'datascience495'
file_name = 'Business_Locations_final.csv'
table_name = 'locations'

engine = sqlalchemy.create_engine(protocol_name+'://'+username+':'+password+'@'+hostname+'/'+database_name)
df = pandas.read_csv(file_name)
df.to_sql(table_name, engine)