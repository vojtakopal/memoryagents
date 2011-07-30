source("magents_experiments.R")

generateOverallGraphs("gng-grid_silent", "GNG+Grid agents without communication")
ggsave(file="gng-grid_silent.eps")   