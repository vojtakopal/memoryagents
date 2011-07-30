source("magents_experiments.R")

generateOverallGraphs("gng-grid-pr-random_silent", "GNG+Grid+PR+Random agents without communication")
ggsave(file="gng-grid-pr-random_silent.eps")   