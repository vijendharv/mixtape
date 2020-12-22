- [Mix Tape](#mix-tape)
  - [Dev Setup](#dev-setup)
    - [Java 11](#java-11)
    - [Maven 3.6.3](#maven-3.6.3)
  - [Build](#build)
  - [Execution](#execution)
    - [Mixtape file](#mixtape-file)
    - [Changes file](#changes-file)
  - [Scaling](#scaling)


# Mix Tape

This is an application (command line) which applies changes from changes.json to mixtape.json    

## Dev Setup

Your local machine needs the following things installed:

### Java 11

We use Java 11 as part of development of Mixtape. You can download [Java 11 from here](https://www.oracle.com/java/technologies/javase-downloads.html)

### Maven 3.6.3

We use Maven as dependency management tool. You can download [Maven 3.6.3 from here](https://maven.apache.org/download.cgi)

## Build:

The code can be built by running 'mvn clean package'. This generates mixtape-1.0.jar 

## Execution:

Once built, the tool can be run using the following command.

java -jar mixtape-1.0.jar *path-to-mixtape-json* *path-to-changes-json*

The result will be an output.json (applying changes to mixtape) and a log file (mixtapechanges.log) with more information. The log file will have error information if any of the changes cannot be applied along with the record data. 

### Mixtape file
Sample [mixtape.json](https://github.com/vijendharv/mixtape/blob/master/mixtape.json) file can be located at the root of this directory

### Changes file
Sample [changes.json](https://github.com/vijendharv/mixtape/blob/master/changes.json) file can be located at the root of this directory

## Scaling

Scaling challenges come in 2 forms.
- Memory: Do we have enough memory on the machine to hold both mixtape.json and changes.json in memory
- Performance: The amount of time it takes to apply the changes 

**Have enough memory on the machine:**
> Assuming we have enough compute: 
> - One of the options is to create additional threads so that we can apply changes in parallel to improve performance.

**Not enough memory on the machine:** 
> Assuming we have enough storage: 
> - Chunk both Songs and Users into multiple files (Note: Songs and Users are not changing - Adding a new User/Song or Updating a User/Song are not one of the operations). Use Jackson/Gson streaming API to accomplish this. Read each chunk as needed when applying the changes. 
> - Read through the changes file as a stream, apply changes to the chunks that were created earlier and combine all teh chunks in the end.
> - This approach is definitely is a drag on the performance.

**We have a bank of machines:**
> - We can break the work across multiple machines to get this done faster.
  
 


