# The code part of my bachelour thesis "Java Virtual Machine multi-debugger proxy server".

This is the source code of my thesis.
The TeX source code of my thesis is available at https://github.com/veikokaap/bachelor-thesis
The final result is available at https://comserv.cs.ut.ee/ati_thesis/datasheet.php?id=61837&year=2018&language=en

## Status

The main part of the proxy server works. 
It is possible to attach the proxy server to a single JVM and then to attach multiple debbugers to the proxy server.
Adding/removing breakpoints mostly works, but this is definitely not a ready product to be actually used for debugging.
As part of the thesis, the most important part was to prove that it is actually even possible to create such a functionality, 
but I currently simply don't have the time to finish it.

There is also a bit of code here that I would like to refactor and that I'm not really proud of that I hope to refactor in the future.

## Building

Run `mvn clean package -DskipTests` to just build the server without running any tests.
