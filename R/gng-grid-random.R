source("magents_experiments.R")

generateOverallGraphs("gng-grid-random", "GNG+Grid+Random agents with communication")
ggsave(file="gng-grid-random.eps")   