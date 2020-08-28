// https://hightech.fm/2019/02/06/information
// Исзодные данные
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

// натуральный логарифм
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

// стандартное отклонение
stdev = { List<? extends Number> ls ->
	def sum = ls.sum()
	def avg = sum / ls.size()
	def acm = 0.0
	ls.each { v -> acm += (avg - v) ** 2 }
	return (acm / ls.size()) ** 0.5
}

println entropy( vals )
println "-"*20

def rvals = [:]
Random rnd = new Random()
for( int i=0; i<100; i++ ){
	[ 10, 20, 50, 100 ].each { r ->
		def ls = rvals[r] ?: []
		ls << rnd.nextInt( r )
		rvals[r] = ls
	}
}

rvals.each { r, ls ->
	println "range $r entropy ${entropy(ls)} stdev ${stdev(ls)}"
}