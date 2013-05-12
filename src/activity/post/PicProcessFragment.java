package activity.post;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import com.ebay.ebayfriend.R;
import com.jhlabs.image.ChromeFilter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import bean.PurchasedItem;
import util.ImageTools;
import util.PicUtil;
import activity.MainActivity;
import activity.login.LoginActivity;
import activity.post.AttachItemFragment.GetItemListThread;
import activity.post.AttachItemFragment.ItemListHandler;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.ContentResolver;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PicProcessFragment extends Fragment {

	protected Bitmap originBitmap, thumbPic;
	private PurchasedItem pi;
	private ImageView imageView;
	private Bitmap myBitmap = null;
	private ImageLoadingListener animateFirstListener = new PicUtil.AnimateFirstDisplayListener();
	private ImageLoader imageLoader;
	private Button btStyle1, btStyle2, btStyle3, btStyle4, postPicNext;
	private String originalPic, styledPic;
	private LinearLayout ll;
	private CustomToast ct;
	private ImageHandler ih;

	public PicProcessFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		initial();

		View view = inflater.inflate(R.layout.process_pic, container, false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.post_title);
		windowTitleView.setText("select picture");

		ll = (LinearLayout) view.findViewById(R.id.style_linearlayout);
		ll.setVisibility(View.INVISIBLE);

		postPicNext = (Button) view.findViewById(R.id.post_pic_next);
		postPicNext.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				switchToVoiceProcessFragment(pi);

			}

		});
		postPicNext.setVisibility(View.INVISIBLE);
		btStyle1 = (Button) view.findViewById(R.id.button_style1);
		btStyle1.setOnClickListener(new ClickListener(1));
		btStyle2 = (Button) view.findViewById(R.id.button_style2);
		btStyle2.setOnClickListener(new ClickListener(2) );
		btStyle3 = (Button) view.findViewById(R.id.button_style3);
		btStyle3.setOnClickListener(new ClickListener(3));
		btStyle4 = (Button) view.findViewById(R.id.button_style4);
		btStyle4.setOnClickListener(new ClickListener(4));
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
								// TODO Auto-generated method stub
								// 这里item是根据选择的方式，
								// 在items数组里面定义了两种方式，拍照的下标为1所以就调用拍照方法
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
				// TODO: handle exception
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
				// TODO: handle exception
			}
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

	class ImageHandler extends Handler {
		public ImageHandler() {

		}

		public ImageHandler(Looper l) {

		}

		@Override
		public void handleMessage(Message msg) {
			Bundle b= msg.getData();
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
					case 1:
						bitmap = ImageTools.toFuDiao(myBitmap);
						break;
					case 2:
						bitmap = ImageTools.toYouHua(myBitmap);
						break;
					case 3:
						bitmap = ImageTools.toGrayscale(myBitmap);
						break;
					default:
						bitmap = myBitmap;

					}
					styledPic = savePic(bitmap);
					
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
}
