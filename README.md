# Running the StreamingGithubEventCounter 
- Make sure that spark is on your path
- Step into the project directory
- Start sbt, run the assembly target
- Submit the job to spark (and hit ctrl + c to terminate it)
 
				spark-submit 
				--class "consumer.StreamingGithubEventCounter" 
				--master "local[*]" 
				--conf "spark.executor.extraJavaOptions=-XX:+PrintGCDetails -XX:+PrintGCTimeStamps" 
				--executor-memory 6G 
				--deploy-mode "client" 
				"file:///path/to/fatjar.jar" 
				"localhost:9092,localhost:9093,localhost:9094" "github"

