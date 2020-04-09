allTracksDf.groupBy("genre").agg(avg("energy"), avg("loudness"), avg("danceability"),
		avg("speechiness"), avg("instrumentalness"), avg("duration_ms")).show()

allTracksDf.groupBy("genre").agg(stddev("energy"), stddev("loudness"), stddev("danceability"),
		stddev("speechiness"), stddev("instrumentalness"), stddev("duration_ms")).show()

allTracksDf.groupBy("genre", "mode").agg(count("mode")).orderBy("genre").show()