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

Now you can access the api at: 

  <http://localhost:9200/>

Run the front-end in apache: You have, to enable http-proxy module, and create a virtual host:

Windows XAMPP:

  edit xampp\apache\conf\httpd.conf

change this:

	#LoadModule proxy_module modules/mod_proxy.so

to:

	LoadModule proxy_module modules/mod_proxy.so

and

	#LoadModule proxy_http_module modules/mod_proxy_http.so

to:

	LoadModule proxy_http_module modules/mod_proxy_http.so

create virtualhost: edit  xampp\apache\conf\extra\httpd-vhosts.conf
		
	NameVirtualHost example.com:80
 		<VirtualHost example.com:80>
  			DocumentRoot "C:/xampp/htdocs/restaurate"
  			ServerName example.com
		ProxyPass /api/ http://localhost:9200/
  			ErrorLog "logs/restaurate-error.log"
	</VirtualHost>

restart XAMPP

Linux Debian/Ubuntu:

	edit /etc/apache2/apache2.conf

change this:
	
	#LoadModule proxy_module modules/mod_proxy.so

to:
	
	LoadModule proxy_module modules/mod_proxy.so

and
	
	#LoadModule proxy_http_module modules/mod_proxy_http.so

to:
	
	LoadModule proxy_http_module modules/mod_proxy_http.so

create virtualhost: add /etc/apache2/sites-available/restaurate
	
	NameVirtualHost example.com:80
 	<VirtualHost example.com:80>
	    DocumentRoot "C:/xampp/htdocs/restaurate"
	  	ServerName example.com
	  	ProxyPass /api/ http://localhost:9200/
	  	ErrorLog "logs/restaurate-error.log"
	</VirtualHost>
	
enable the virtualhost:

	root@host:~# a2ensite restaurate

restart apache

	root@host:~# /etc/init.d/apache2 restart
	
Enjoy!	