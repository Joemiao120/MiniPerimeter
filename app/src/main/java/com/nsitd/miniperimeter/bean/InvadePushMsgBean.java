package com.nsitd.miniperimeter.bean;

public class InvadePushMsgBean {

	public String channel;
	/*打开abVideoOpen 关闭abVideoClose 结束event_over*/
	public String command;
	public String defence_id;
	public String defence_name;
	public String event_status;
	public String event_type;
	public String flag;
	public String password;
	public String port;
	public String position;
	public String resource_id;
	public String resource_ip;
	public String time;
	public String time_str;
	public String uid;
	public String username;
	public String x;
	public String y;
	public String z;
	@Override
	public String toString() {
		return "MessagePushBean [channel=" + channel + ", command=" + command
				+ ", defence_id=" + defence_id + ", defence_name="
				+ defence_name + ", event_status=" + event_status
				+ ", event_type=" + event_type + ", flag=" + flag
				+ ", password=" + password + ", port=" + port + ", position="
				+ position + ", resource_id=" + resource_id + ", resource_ip="
				+ resource_ip + ", time=" + time + ", time_str=" + time_str
				+ ", uid=" + uid + ", username=" + username + ", x=" + x
				+ ", y=" + y + ", z=" + z + "]";
	}
	
}
