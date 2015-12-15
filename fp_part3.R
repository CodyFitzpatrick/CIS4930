install.packages("klaR")
library("klaR")

imdbData <- read.table('output3.txt',  sep="\t", comment.char = "", quote = "\"", header = TRUE)
imdbData <- imdbData[,-1, drop = FALSE]
imdbData$Rating = round(imdbData$Rating, digits = 0)

imdb_kmodes <- kmodes(imdbData, 28, iter.max = 1)
plot(imdbData, imdb_kmodes$cluster)