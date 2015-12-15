install.packages("klaR")
install.packages("cluster")
library("klaR")
library("cluster")

imdbData <- read.table('output3.txt',  sep="\t", comment.char = "", quote = "\"", header = TRUE)
drops <- c("Title", "Genre")
imdbData <- imdbData[,!names(imdbData) %in% drops]
imdbData$Rating = round(imdbData$Rating, digits = 0)

imdb_kmodes <- kmodes(imdbData, 28, iter.max = 10)
table(imdb_kmodes2$clustering, imdbData$Genre)

#imdb_kmodes2 <- pam(daisy(imdbData, metric="gower", type=list(ordratio=1:7)), k=28)
