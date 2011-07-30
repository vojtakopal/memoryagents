source("magents_experiments.R")

generateOverallGraphs("gng-grid", "GNG+Grid agents with communication")
ggsave(file="gng-grid.eps")   