source("magents_experiments.R")

generateSingleGraph("grid_silent", "Grid agents without communication")
ggsave(file="grid_silent.eps")  