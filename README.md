RestauRate
==========
This software is designed for community restaurant rating.

<b>Requirements: </b>

<pre><code>Maven >= 2
JavaJDK >= 1.6.10
Postgresql >= 8.4
</code></pre>

<b>Intsalling:</b>
==========
Adding new user and database for postgresql:
Windows: open pgAdmin:

	Login Roles – New Login Role
	Databases – New Database, set the owner!

Linux (the easy way.):

	root@host:~# su - postgres
	postgres@host:~$ CREATE USER restaurate WITH PASSWORD 'myPassword';
	postgres@host:~$ CREATE DATABASE restaurate;
	postgres@host:~$ GRANT ALL PRIVILEGES ON DATABASE restaurate to restaurate;

edit your api/local.yml file for the database what you just set.

compile and make jar file.

	 cd api
	 mvn package

create init db struckture

	cd api
	java -jar tartget/restaurant-rate.jar db migrate local.yml

run the server

	java -jar tartget/restaurant-rate.jar server local.yml

Now you can access to the page at: 

  <http://localhost:9200/>

Enjoy!	
