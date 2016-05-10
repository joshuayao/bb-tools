# bb-tools
It's a kit which is used to collect application level profiling data for TPCx-BB HoMR and HoS engines

## How to run
bin/bb-tools [property file path]

We provide 2 sample property files under conf/


## Settings
### Common Settings
bb.log.dir                            path of TPCx-BB logs directory

bb.engine                             [homr|hos] hive on mapreduce or hive on spark

history.server.host                   hostname of yarn,spark historyserver

history.server.port                   port of yarn,spark history server

### HoMR Settings
yarn.restapi.url.hs.job.counter       mr restapi url template
log.regexp.app.id.mr                  reg exp. for getting mr job id

### HoS Settings
spark.restapi.url.hs.stages           spark restapi url template
log.regexp.app.id.spark               reg exp. for getting spark app id

## How to build
mvn clean install package
