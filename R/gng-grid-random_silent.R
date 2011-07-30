source("magents_experiments.R")

generateOverallGraphs("gng-grid-random_silent", "GNG+Grid+Random agents without communication")
ggsave(file="gng-grid-random_silent.eps")   