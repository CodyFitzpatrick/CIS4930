install.packages("klaR")
install.packages("cluster")
install.packages("vwr")
library("klaR")
library("cluster")
library("vwr")

imdbData <- read.table('output3.txt',  sep="\t", comment.char = "", quote = "\"", header = TRUE)
genres <- imdbData$Genre
drops <- c("Genre")
imdbData <- imdbData[,!names(imdbData) %in% drops]
imdbData$Rating = round(imdbData$Rating, digits = 0)


#method 1 - k-modes algorithm (may be our best bet currently)
# imdb_kmodes <- kmodes(imdbData, 28, iter.max = 10)
# table(imdb_kmodes2$clustering, imdbData$Genre)

#method 2 - string distance algorithm (Jaro Winkler distance algorithm)
#cluster based on Genre and Country and Director
imdbData$YearAndCountryAndDirector <- paste0(as.character(imdbData$Year), " ", as.character(imdbData$Country), " ", as.character(imdbData$Director))
uniques <- unique(as.character(imdbData$YearAndCountryAndDirector))
distance <- stringdistmatrix(uniques, uniques, method = "jw")
rownames(distance) <- uniques
hc <- hclust(as.dist(distance))
dfClust <- data.frame(uniques, cutree(hc, k=28))
names(dfClust) <- c('genrename', 'cluster')
plot(table(dfClust$cluster))

imdbData <- read.table('output3.txt',  sep="\t", comment.char = "", quote = "\"", header = TRUE)
#doesn't work
#table(dfClust$cluster, imdbData$Genre)
