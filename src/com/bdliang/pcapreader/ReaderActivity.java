package com.bdliang.pcapreader;

import java.util.ArrayList;

import org.jnetpcap.Pcap;
import org.jnetpcap.packet.JPacket;
import org.jnetpcap.packet.PcapPacket;
import org.jnetpcap.packet.PcapPacketHandler;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class ReaderActivity extends ListActivity {

	private String file = "/mnt/sdcard/shark/shark_capture.pcap";

	// ArrayList which will contain the parsed packets.
	private ArrayList<JPacket> packets = new ArrayList<JPacket>();

	// Custom adapter for an ArrayList of JPacket.
	private JPacketAdapter p_adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reader);
		loadData(file);
		// Binding the ArrayAdapter with the view for each row of the ListView.
		p_adapter = new JPacketAdapter(this, R.layout.list_item, packets);

		// Binding the ArrayAdapter with the ListView.
		setListAdapter(p_adapter);
	}

	/**
	 * @param pcapFile
	 */
	private void loadData(String pcapFile) {
		StringBuilder errbuf = new StringBuilder();
		Pcap pcap = Pcap.openOffline(pcapFile, errbuf);
		if (pcap == null) {
			Log.e("ReaderActivity", "Error while opening device for capture: "
					+ errbuf.toString());
			return;
		}

		/***************************************************************************
		 * 创建用来接收数据包handler
		 **************************************************************************/
		PcapPacketHandler<String> jpacketHandler = new PcapPacketHandler<String>() {

			@Override
			public void nextPacket(PcapPacket packet, String user) {
				// TODO Auto-generated method stub
				Log.d("ReaderActivity", ""
						+ packet.getCaptureHeader().timestampInMillis());
				packets.add(packet);
			}
		};

		pcap.loop(-1, jpacketHandler, null);
		pcap.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.reader, menu);
		return true;
	}

}
