source("magents_experiments.R")

generateOverallGraphs("gng-grid-pr-random", "GNG+Grid+PR+Random agents with communication")
ggsave(file="gng-grid-pr-random.eps")   