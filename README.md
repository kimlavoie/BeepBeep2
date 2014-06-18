BEEPBEEP 2, CEP edition (in construction)

To test, you must have python, ruby and perl accessible via their usual command:

python[file]
ruby [file]
perl [file]

You also have to install Apache Ant. To run the test, simply type:
ant

Status:
For now, the system can load processor classes reflectively and call external scripts. The support for external scripts is python, ruby and perly only. Other executables might works, but it is not guaranteed. Look at scripts in the external folder to see how to construct them.

TODO:
Add a parser to read ESQL files.
Add a factory to build the processor graph from the parse tree generated.
Add basic processors.
