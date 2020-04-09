cormat = read.table("correlationMatrix.txt")
colnames(cormat) = c("danceability", "energy", "key", "loudness", "mode",
  				"speechiness","acousticness","instrumentalness","liveness",
  				"valence", "tempo", "duration_ms", "time_signature")
rownames(cormat) = c("danceability", "energy", "key", "loudness", "mode",
  				"speechiness","acousticness","instrumentalness","liveness",
  				"valence", "tempo", "duration_ms", "time_signature")

library(ggcorrplot)
ggcorrplot(cormat)