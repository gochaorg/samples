package org.example.stat

trait SampleStatCompute implements SampleValues<Number> {
    SampleStat getStat(){
        SampleStat ss = new SampleStat()
        List<Number> numbers = getSampleValues()

        ss.count = numbers.size()

        if( !numbers.isEmpty() ){
            numbers = numbers.sort(false)
            Double min = null
            Double max = null
            Double sum = 0
            for( Number n : numbers ){
                min = min==null ? n.toDouble() : (min > n ? n.toDouble() : min)
                max = max==null ? n.toDouble() : (max < n ? n.toDouble() : max)
                sum += n.toDouble()
            }
            ss.min = min
            ss.max = max
            ss.sum = sum
            ss.avg = sum / ss.count

            [ [0.1, 'centile10' ]
            , [0.25, 'centile25']
            , [0.5, 'centile50']
            , [0.682, 'centile68' ]
            , [0.75, 'centile75']
            , [0.9, 'centile90']
            , [0.954, 'centile95']
            , [0.99, 'centile99']
            , [0.9973, 'centile9973'] ].each {trg ->
                double cent = trg[0] as Double
                String prop = trg[1] as String

                int idx = (numbers.size() * cent) as int
                Double n = numbers[idx].toDouble()
                ss[prop] = n
            }

            Double devSum = 0
            for( Number n : numbers ){
                devSum += ( n.toDouble() - ss.avg ) ** 2
            }

            ss.stdev = (devSum ** 0.5) / (ss.count > 1 ? ss.count-1 : ss.count)
        }

        return ss
    }
}