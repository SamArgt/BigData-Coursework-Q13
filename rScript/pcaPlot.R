df0 = read.csv("part0.csv", sep=",", header=FALSE, col.names = c("genre", "pca1", "pca2"))
df1 = read.csv("part1.csv", sep=",", header=FALSE, col.names = c("genre", "pca1", "pca2"))
df2 = read.csv("part2.csv", sep=",", header=FALSE, col.names = c("genre", "pca1", "pca2"))
df3 = read.csv("part3.csv", sep=",", header=FALSE, col.names = c("genre", "pca1", "pca2"))
df4 = read.csv("part4.csv", sep=",", header=FALSE, col.names = c("genre", "pca1", "pca2"))

df = rbind(df0, df1, df2, df3, df4)

library(ggplot2)

ggplot(data=df, mapping=aes(x=pca1, y=pca2, color=genre))+
	geom_point()
