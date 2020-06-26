// https://hightech.fm/2019/02/06/information
def vals = [ 1, 2, 3, 2, 3 , 4, 1 , 2 , 3 , 5, 1 , 4, 2 ]
println vals

// Расчет гистограммы
histo = { list -> 
	def histo = [:]
	list.each { v ->
		def cnt = histo[ v ] ?: 0.0
		histo[ v ] = cnt + 1.0
	}
	histo
}

println histo( vals )

log2 = { double v -> 
	Math.log(v) / Math.log(2)
}

// Расчет энтропии
entropy = { List<?> list ->
	def hist = histo( list )
	def cnt = list.size() as double
	def H = 0.0
	hist.each { v, vcnt ->
		def p = vcnt / cnt
		def h = p * log2(p)
		H += h
	}
	return -H
}

println entropy( vals )
