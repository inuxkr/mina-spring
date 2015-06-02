<h2><font color='green'>New remote service implementation</font></h2>
<h3>Based on <a href='http://mina.apache.org/'>apache mina</a> and <a href='http://static.springframework.org/spring/docs/2.0.x/reference/remoting.html'>springframework remoteservice exporter</a></h3>


<ul> Features<br>
<li> Export remote service with usual POJO.</li>
<li> Use mina NIO Socket Protocol for durable connection.</li>
<li> Synchronize call at client.</li>
<li> Failover mechanism when client disconnected.</li>
</ul>