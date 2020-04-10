df0 = read.csv("part0.csv", sep=",", header=FALSE, col.names = c("genre", "PC1","PC2","PC3"))
df1 = read.csv("part1.csv", sep=",", header=FALSE, col.names = c("genre", "PC1","PC2","PC3"))
df2 = read.csv("part2.csv", sep=",", header=FALSE, col.names = c("genre", "PC1","PC2","PC3"))
df3 = read.csv("part3.csv", sep=",", header=FALSE, col.names = c("genre", "PC1","PC2","PC3"))
df4 = read.csv("part4.csv", sep=",", header=FALSE, col.names = c("genre", "PC1","PC2","PC3"))

df = rbind(df0, df1, df2, df3, df4)

library(ggplot2)

ggplot(data=df, mapping=aes(x=PC1, y=PC2, color=genre))+
	geom_point(size=2)+
	theme(legend.text=element_text(size=16),
			axis.title=element_text(size=15),
			legend.title=element_text(size=17))
