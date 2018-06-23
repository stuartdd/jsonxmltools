# json_tools
Wrappers for Jackson JSON tools and loading configuration data from JSOM files

Also provides XML tools (Addes to support the Remotelogger project)

I will rename to jsonxmltools when I have time.

This is a gradle project. To use it as a dependency (e.g for the remote logger) 
```
git clone http://github.com/stuartdd/json_tools
```
for windows:
```
gradlew.bat clean publish
```
for Linux:
```
./ gradlew clean publish
```

* Project uses mavenLocal as a repository.

Add a dependency as follows:
```
dependencies {
    compile 'stuartdd:jsonxmltools:1.0'
}
```
