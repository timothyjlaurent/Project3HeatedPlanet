 Heated Planet : 
 Team 16 :
 Derek Finlinson
 Timothy Laurent
 Aditi Mehta
 Joseph Slater

 Usage:

 build database tables

in the project root run the `createdb.sh` script;

```
$ sh ./createdb.sh
```
and then enter the password for the root; on gatech VM this is `root`

 compile classes

Recommended to compile in Eclipse. We had difficulty reliably building the project with javac. We have included a compiled .Jar file for youe convenience


 run program

```
$ cd jar
$ java -jar Demo.jar [ARGS]
```

- Command line arguments
-- -g geographic precision - Percentage of grid points to keep (1-100) : Default 100
-- -t temporal precision - Percentage of time points to keep (1-100) : Default 100
-- -p data precision - precision of numeric data - ie number of decimal places to keep : Default 5

 User interface

- Simulation interface
select simulation parameters: 
then push run simulation.

- Query Interface 
select Sample, coordinate latitudes and longitudes, and select time interval. 
Run the query and the simulation will be run through the interpolation filter and then the results will be displated to the user
The Interpolation step can be compute-intensive so please be patient while it computes



