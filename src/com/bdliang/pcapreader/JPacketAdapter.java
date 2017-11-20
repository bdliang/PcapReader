package com.bdliang.pcapreader;

import java.util.ArrayList;

import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.format.FormatUtils;
import org.jnetpcap.protocol.network.Icmp;
import org.jnetpcap.protocol.network.Ip4;
import org.jnetpcap.protocol.network.Ip6;
import org.jnetpcap.protocol.tcpip.Http;
import org.jnetpcap.protocol.tcpip.Tcp;
import org.jnetpcap.protocol.tcpip.Udp;
import org.jnetpcap.protocol.voip.Rtp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class JPacketAdapter extends ArrayAdapter<JPacket> {
	private ArrayList<JPacket> packets;
	private Context context;

	public JPacketAdapter(Context context, int resource,
			ArrayList<JPacket> packets) {
		super(context, resource, packets);
		// TODO Auto-generated constructor stub
		this.context = context;
		this.packets = packets;
	}

	Ip4 ip4 = new Ip4();
	Ip6 ip6 = new Ip6();
	Tcp tcp = new Tcp();
	Udp udp = new Udp();
	Icmp icmp = new Icmp();
	Http http = new Http();
	Rtp rtp = new Rtp();

	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.list_item, null);
		}
		JPacket p = packets.get(position);
		if (p != null) {

			TextView network_protocol = (TextView) v
					.findViewById(R.id.network_protocol_text);
			TextView src_network = (TextView) v
					.findViewById(R.id.src_network_text);
			TextView dst_network = (TextView) v
					.findViewById(R.id.dst_network_text);

			TextView transport_protocol = (TextView) v
					.findViewById(R.id.transport_protocol_text);
			TextView src_transport = (TextView) v
					.findViewById(R.id.src_transport_text);
			TextView dst_transport = (TextView) v
					.findViewById(R.id.dst_transport_text);

			TextView application_protocol = (TextView) v
					.findViewById(R.id.application_protocol_text);

			TextView packet_number = (TextView) v
					.findViewById(R.id.packet_number_text);

			packet_number.setText(Integer.toString(position + 1));

			if (p.hasHeader(Ip4.ID)) {
				p.getHeader(ip4);
				network_protocol.setText("IPv4");
				src_network.setText(FormatUtils.ip(ip4.source()));
				dst_network.setText(FormatUtils.ip(ip4.destination()));

				if (p.hasHeader(Tcp.ID)) {
					p.getHeader(tcp);

					transport_protocol.setText("TCP Seq::" + tcp.seq());
					src_transport.setText(Integer.toString(tcp.source()));
					dst_transport.setText(Integer.toString(tcp.destination()));

					if (p.hasHeader(Http.ID)) {
						p.getHeader(http);
						application_protocol.setText("HTTP");
					} else if (p.hasHeader(Rtp.ID)) {
						p.getHeader(rtp);
						application_protocol.setText("RTP");
					} else
						application_protocol.setText(context
								.getString(R.string.unknown));
				} else if (p.hasHeader(Udp.ID)) {
					p.getHeader(udp);
					transport_protocol.setText("UDP");
					src_transport.setText(Integer.toString(udp.source()));
					dst_transport.setText(Integer.toString(udp.destination()));
					if (p.hasHeader(Rtp.ID)) {
						p.getHeader(rtp);
						application_protocol.setText("RTP");
					}
				}
			} else if (p.hasHeader(Icmp.ID)) {
				p.getHeader(icmp);
				network_protocol.setText("ICMP");
				src_network.setText(context.getString(R.string.unknown));
				dst_network.setText(context.getString(R.string.unknown));
			} else if (p.hasHeader(Ip6.ID)) {
				p.getHeader(ip6);
				network_protocol.setText("IPv6");
				src_network
						.setText(FormatUtils.asStringIp6(ip6.source(), true));
				dst_network.setText(FormatUtils.asStringIp6(ip6.destination(),
						true));
				if (p.hasHeader(Tcp.ID)) {
					p.getHeader(tcp);
					transport_protocol.setText("TCP");
					src_transport.setText(Integer.toString(tcp.source()));
					dst_transport.setText(Integer.toString(tcp.destination()));

					if (p.hasHeader(Http.ID)) {
						p.getHeader(http);
						application_protocol.setText("HTTP");
					} else if (p.hasHeader(Rtp.ID)) {
						p.getHeader(rtp);
						application_protocol.setText("RTP");
					} else
						application_protocol.setText(context
								.getString(R.string.unknown));
				}

				else if (p.hasHeader(Udp.ID)) {
					p.getHeader(udp);
					transport_protocol.setText("UDP");
					src_transport.setText(Integer.toString(udp.source()));
					dst_transport.setText(Integer.toString(udp.destination()));
					if (p.hasHeader(Rtp.ID)) {
						p.getHeader(rtp);
						application_protocol.setText("RTP");
					}
				}
			}
		}
		return v;
	}
}
