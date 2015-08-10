package com.example.soapcliente;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private EditText nro1, nro2;
	private Button sumar;
	private String resultado = "";
	private TareaSumar tareaSumar = null;
	private TextView tv;
	// 定义SoapObject对象
	private SoapObject detail;
	private String detailResult = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		nro1 = (EditText) findViewById(R.id.nro1);
		nro2 = (EditText) findViewById(R.id.nro2);
		tv = (TextView) findViewById(R.id.tv);
		sumar = (Button) findViewById(R.id.suma);

		sumar.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				detailResult = "";
				Toast.makeText(getApplicationContext(), "正在请求请稍后",
						Toast.LENGTH_LONG).show();
				tareaSumar = new TareaSumar();
				tareaSumar.execute();
			}
		});
	}

	private class TareaSumar extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			// WebService - Opciones
			final String NAMESPACE = "http://WebXml.com.cn/";
			final String URL = "http://webservice.webxml.com.cn/WebServices/TrainTimeWebService.asmx";
			final String METHOD_NAME = "getStationAndTimeByStationName";
			final String SOAP_ACTION = "http://WebXml.com.cn/getStationAndTimeByStationName";

			SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

			request.addProperty("StartStation", nro1.getText().toString()
					.trim());
			request.addProperty("ArriveStation", nro2.getText().toString()
					.trim());
			request.addProperty("UserID", " ");

			SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
					SoapEnvelope.VER11);
			envelope.dotNet = true;
			envelope.bodyOut = request;
			envelope.setOutputSoapObject(request);
			try {
				HttpTransportSE ht = new HttpTransportSE(URL);
				ht.call(SOAP_ACTION, envelope);
				// SoapPrimitive response = (SoapPrimitive)
				// envelope.getResponse();
				SoapObject soapObject = (SoapObject) envelope.bodyIn;
				int count = soapObject.getPropertyCount();
				for (int i = 0; i < count; i++) {
					SoapObject soapChilds = (SoapObject) soapObject
							.getProperty(i);
					for (int j = 0; j < soapChilds.getPropertyCount(); j++) {
						SoapObject soapChilds1 = (SoapObject) soapChilds
								.getProperty(j);
						if (j == 1)
							for (int k = 0; k < soapChilds1.getPropertyCount(); k++) {
								SoapObject soapChilds11 = (SoapObject) soapChilds1
										.getProperty(k);
								for (int l = 0; l < soapChilds11
										.getPropertyCount(); l++) {
									detail = (SoapObject) soapChilds11
											.getProperty(l);
									detailResult = detailResult
											+ "车次："
											+ detail.getPropertyAsString(0)
											+ "\n   始发站："
											+ detail.getPropertyAsString(1)
											+ "  ————   终点站："
											+ detail.getPropertyAsString(2)
											+ "\n\t发车站："
											+ detail.getPropertyAsString(3)
											+ "\t\t\t\t 发车时间："
											+ detail.getPropertyAsString(4)
											+ "\n\t到达站："
											+ detail.getPropertyAsString(5)
											+ "\t\t\t\t 到达时间："
											+ detail.getPropertyAsString(6)
											+ "\n   里程(KM):"
											+ detail.getPropertyAsString(7)
											+ "\t\t\t\t 历时:"
											+ detail.getPropertyAsString(8)
											+ "\n -----------------------------------------------------------------------------"
											+ "\n";
									System.out.println("detail-" + i + "-" + j
											+ "-" + k + "-" + l + "--"
											+ detailResult);
								}
								System.out.println("soapChilds11--" + k + "--"
										+ soapChilds11.toString());
							}
						// System.out.println("soapChilds1--" + j + "--"
						// + soapChilds1.toString());
					}
					// System.out.println("soapChilds--" + i + "--"
					// + soapChilds.toString());
				}
				// SoapObject detail1 = (SoapObject) object.getProperty(0);
				// SoapObject detail2 = (SoapObject) detail1
				// .getPropertySafely("getStationAndTime");
				// detail = (SoapObject) detail2.getProperty("TimeTable");
				// System.out.println("count=" + detail.getPropertyCount());
				// Log.i("Resultado: ", detail.toString());
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			if (success == false) {
				Toast.makeText(getApplicationContext(), "Error",
						Toast.LENGTH_LONG).show();
			} else {
				tv.setText("结果：\n" + detailResult);
				// Toast.makeText(getApplicationContext(),
				// "El resultado es: " + detailResult, Toast.LENGTH_LONG)
				// .show();
			}
		}

		@Override
		protected void onCancelled() {
			Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG)
					.show();
		}
	}

	// public static String getWorkOn(SoapObject obj) {
	// String info = "";
	// try {
	// if (obj.getPropertyCount() > 1) {// 有中心信息
	// int len = obj.getPropertyCount() - 1;
	// for (int i = 0; i < len; i++) {
	// SoapObject child = (SoapObject) obj.getProperty(0);
	// String idStr = child.getPropertyAsString(0);
	// String content = child.getPropertyAsString(1);
	// String sender = child.getPropertyAsString(2);
	// String typeStr = child.getPropertyAsString(3);
	// // String uId = child.getPropertyAsString(4);
	// // String time = child.getPropertyAsString(5);// 时间
	// // long id = Long.parseLong(idStr);
	// // int type = Integer.parseInt(typeStr);
	// info = idStr + "-----" + content + "-------" + sender
	// + "--------" + typeStr;
	// System.out.println("child---" + info);
	// }
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// return info;
	// }
}
