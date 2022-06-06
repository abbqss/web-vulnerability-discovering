try:
    import pymysql.cursors
    from os import system
    from sys import argv
except:
    system("pip install pymysql")
    import pymysql.cursors

db_name = raw_input("db_name:\n") if len(argv) < 2 else argv[1]

connection  = pymysql.connect(host='localhost',
             user='root',
             password='root',
             db=db_name,
             cursorclass=pymysql.cursors.DictCursor)

cursor = connection .cursor()
cursor.execute('SHOW TABLES')
res = cursor.fetchall()
tables = [x["Tables_in_mysql"] for x in res]
system("mkdir %s" %db_name)
for table in tables:
    print "mysqldump -uroot -proot {db_name} {table_name} --skip-comments --skip-add-locks > {db_name}\{table_name}.sql".format(db_name=db_name,table_name=table)
    system("mysqldump -uroot -proot {db_name} {table_name} --skip-comments --skip-add-locks > {db_name}\{table_name}.sql &".format(db_name=db_name,table_name=table))
