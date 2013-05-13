package activity.post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import util.ImageTools;
import util.PicUtil;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import bean.PurchasedItem;

import com.ebay.ebayfriend.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class PicProcessFragment extends Fragment {

	protected Bitmap originBitmap, thumbPic;
	private PurchasedItem pi;
	private ImageView imageView;
	private Bitmap myBitmap = null;
	private ImageLoadingListener animateFirstListener = new PicUtil.AnimateFirstDisplayListener();
	private ImageLoader imageLoader;
	private Button postPicNext;
	private String originalPic, styledPic;
	private LinearLayout ll;
	private CustomToast ct;
	private ImageHandler ih;
	private GridView gv;
	private LayoutInflater inflater;

	private List<String> dataList = new ArrayList<String>();

	public PicProcessFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		initial();

		this.inflater = inflater;
		View view = inflater.inflate(R.layout.process_pic, container, false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.post_title);
		windowTitleView.setText("select picture");

		ll = (LinearLayout) view.findViewById(R.id.style_linearlayout);
		ll.setVisibility(View.INVISIBLE);

		gv = (GridView) view.findViewById(R.id.picture_grid_view);

		GridViewAdapter adapter = new GridViewAdapter();

		gv.setAdapter(adapter);
		int size = dataList.size();
		DisplayMetrics dm = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		float density = dm.density;
		int allWidth = (int) (110 * size * density);
		int itemWidth = (int) (100 * density);
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
				allWidth, LinearLayout.LayoutParams.MATCH_PARENT);
		gv.setLayoutParams(layoutParams);
		gv.setColumnWidth(itemWidth);
		gv.setHorizontalSpacing(10);
		gv.setStretchMode(GridView.NO_STRETCH);
		gv.setNumColumns(size);

		postPicNext = (Button) view.findViewById(R.id.post_pic_next);
		postPicNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchToVoiceProcessFragment(pi);

			}

		});
		postPicNext.setVisibility(View.INVISIBLE);
		// btStyle1 = (Button) view.findViewById(R.id.button_style1);
		// btStyle1.setOnClickListener(new ClickListener(1));
		// btStyle2 = (Button) view.findViewById(R.id.button_style2);
		// btStyle2.setOnClickListener(new ClickListener(2));
		// btStyle3 = (Button) view.findViewById(R.id.button_style3);
		// btStyle3.setOnClickListener(new ClickListener(3));
		// btStyle4 = (Button) view.findViewById(R.id.button_style4);
		// btStyle4.setOnClickListener(new ClickListener(4));
		imageView = (ImageView) view.findViewById(R.id.post_pic);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final CharSequence[] items = { "from gallery", "from camera" };

				ll.setVisibility(View.VISIBLE);
				postPicNext.setVisibility(View.VISIBLE);

				AlertDialog dlg = new AlertDialog.Builder(
						PicProcessFragment.this.getActivity())
						.setTitle("Choose Picture")
						.setItems(items, new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (which == 1) {
									Intent getImageByCamera = new Intent(
											"android.media.action.IMAGE_CAPTURE");
									File f = new File(Environment
											.getExternalStorageDirectory()
											+ File.separator + "temp");
									Uri imageUri = Uri.fromFile(f);
									getImageByCamera.putExtra(
											MediaStore.EXTRA_OUTPUT, imageUri);
									f.deleteOnExit();
									startActivityForResult(getImageByCamera, 1);
								} else {
									Intent getImage = new Intent(
											Intent.ACTION_GET_CONTENT);
									getImage.addCategory(Intent.CATEGORY_OPENABLE);
									getImage.setType("image/jpeg");
									startActivityForResult(getImage, 0);
								}

							}
						}).create();
				dlg.show();
			}

		});
		return view;
	}

	private void initial() {
		Bundle b = getArguments();
		pi = (PurchasedItem) b.getSerializable("purchasedItem");
		ct = new CustomToast(getActivity(), "Processing");

		dataList.add("Emboss");
		dataList.add("Painting");
		dataList.add("Gray");
		dataList.add("Blur");
		dataList.add("Original");

		imageLoader = ImageLoader.getInstance();
		ih = new ImageHandler();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		// ContentResolver contentResolver = getActivity().getContentResolver();
		/**
		 * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断
		 * ，这里的requestCode跟startActivityForResult里面第二个参数对应
		 */

		if (requestCode == 0) {
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getActivity().getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				originalPic = cursor.getString(columnIndex);
				cursor.close();

				if (myBitmap != null && !myBitmap.isRecycled()) {
					myBitmap.recycle();
				}
				myBitmap = BitmapFactory.decodeFile(originalPic);

				// imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
				prepareImg(originalPic);
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				// Bundle extras = data.getExtras();
				// myBitmap = (Bitmap) extras.get("data");
				// ByteArrayOutputStream baos = new ByteArrayOutputStream();
				// myBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);

				myBitmap = BitmapFactory.decodeFile(Environment
						.getExternalStorageDirectory()
						+ File.separator
						+ "temp");
				originalPic = savePic(myBitmap);
				if (myBitmap != null && !myBitmap.isRecycled()) {
					myBitmap.recycle();
				}
				myBitmap = BitmapFactory.decodeFile(originalPic);

				prepareImg(originalPic);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	private void recycleBitmap(Bitmap bitmap){
		if(bitmap!=null&&!bitmap.isRecycled()){
			bitmap.recycle();
			bitmap = null;
		}
	}

	private String savePic(Bitmap bitmap) {
		// String fileName = Environment.getExternalStorageDirectory().getPath()
		// + File.separator + (new Date()).toString();
		String fileName = (new Date()).toString();
		fileName = fileName.replace(' ', '_').replace(':', '_')
				.replace('+', '_');
		FileOutputStream b = null;
		try {
			b = getActivity().openFileOutput(fileName, Context.MODE_PRIVATE);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				b.flush();
				b.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return getActivity().getFilesDir().toString() + File.separator
				+ fileName;
	}

	private void prepareImg(String picUrl) {
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.ic_stub)
				.showImageForEmptyUri(R.drawable.ic_empty)
				.showImageOnFail(R.drawable.ic_error).cacheInMemory()
				.cacheOnDisc().displayer(new RoundedBitmapDisplayer(20))
				.build();
		imageLoader.displayImage("file://" + picUrl, imageView, options,
				animateFirstListener);
	}

	public static Bitmap getPicFromBytes(byte[] bytes,
			BitmapFactory.Options opts) {
		if (bytes != null)
			if (opts != null)
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
						opts);
			else
				return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
		return null;
	}

	public static byte[] readStream(InputStream in) throws Exception {
		byte[] buffer = new byte[1024];
		int len = -1;
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();

		while ((len = in.read(buffer)) != -1) {
			outStream.write(buffer, 0, len);
		}
		byte[] data = outStream.toByteArray();
		outStream.close();
		in.close();
		return data;
	}

	public void setBitmap(Bitmap bitmap) {
		this.originBitmap = bitmap;
	}

	public Bitmap getThumbPic() {
		return this.thumbPic;
	}

	public Bitmap getOriginBitmap() {
		return this.originBitmap;
	}

	public Bitmap getBitmapStyle1() {
		return null;
	}

	public Bitmap getBitmapStyle2() {
		return null;
	}

	public Bitmap getBitmapStyle3() {
		return null;
	}

	private void switchToVoiceProcessFragment(PurchasedItem pi) {
		VoiceProcessFragment fragment = new VoiceProcessFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("purchasedItem", pi);
		fragment.setArguments(bundle);
		FragmentTransaction transaction = getFragmentManager()
				.beginTransaction();
		transaction.replace(R.id.post_content, fragment);
		transaction.addToBackStack(null);
		transaction.commit();
		Log.e("postPicProcessFragment", "switch to voiceProcessFragment");
	}

	@SuppressLint("HandlerLeak")
	class ImageHandler extends Handler {
		public ImageHandler() {

		}

		public ImageHandler(Looper l) {

		}

		@Override
		public void handleMessage(Message msg) {
			Bundle b = msg.getData();
			String styledPic = b.getString("bitmap");
			prepareImg(styledPic);
			ct.stopToast();
		}
	}

	class ClickListener implements OnClickListener {

		private int style;

		public ClickListener(int i) {
			style = i;
		}

		@Override
		public void onClick(View v) {

			ct.showToast(999999);
			Thread t = new Thread() {
				@Override
				public void run() {
					Bitmap bitmap;
					switch (style) {
					case 0:
						bitmap = ImageTools.toFuDiao(myBitmap);
						break;
					case 1:
						bitmap = ImageTools.toYouHua(myBitmap);
						break;
					case 2:
						bitmap = ImageTools.toGrayscale(myBitmap);
						break;
					case 3:
						bitmap = ImageTools.toJiMu(myBitmap);
						break;
					default:
						bitmap = myBitmap;

					}
					styledPic = savePic(bitmap);
					switch (style) {
					case 0:
					case 1:
					case 2:
					case 3:
						recycleBitmap(bitmap);
						break;
					default:

					}

					Message msg = new Message();
					Bundle b = new Bundle();
					b.putString("bitmap", styledPic);
					msg.setData(b);
					ih.sendMessage(msg);
				}
			};
			t.start();
		}

	}

	class GridViewAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return dataList.size();
		}

		@Override
		public String getItem(int position) {
			return dataList.get(position - 1);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = inflater.inflate(R.layout.grid_view_item, null);
			Button button = (Button) convertView
					.findViewById(R.id.button_style);
			button.setText(dataList.get(position));
			button.setOnClickListener(new ClickListener(position));
			return convertView;
		}

	}
}
