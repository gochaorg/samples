package org.example.ha.tezt

import groovy.transform.EqualsAndHashCode
import groovy.transform.Sortable
import groovy.transform.ToString

@ToString(includeNames = true)
@EqualsAndHashCode(includes = ['host','port'])
@Sortable(includes = ['host','port'])
class SocketAddr {
    String host
    int port
}
