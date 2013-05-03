package activity.post;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import com.ebay.ebayfriend.R;

import bean.PurchasedItem;
import util.PicUtil;
import activity.MainActivity;
import activity.post.AttachItemFragment.GetItemListThread;
import activity.post.AttachItemFragment.ItemListHandler;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class PicProcessFragment extends Fragment {

	protected Bitmap originBitmap, thumbPic;
	private PicUtil picUtil;
	private PurchasedItem pi;
	private ImageView imageView;
	private Bitmap myBitmap;
	private byte[] mContent;

	public PicProcessFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		Bundle b = getArguments();
		pi = (PurchasedItem) b.getSerializable("purchasedItem");

		View view = inflater.inflate(R.layout.process_pic, container, false);
		TextView windowTitleView = (TextView) getActivity().findViewById(
				R.id.post_title);
		windowTitleView.setText("select picture");
		imageView = (ImageView) view.findViewById(R.id.post_pic);
		imageView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				final CharSequence[] items = { "from gallery", "from camera" };

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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		ContentResolver contentResolver = getActivity().getContentResolver();
		/**
		 * 因为两种方式都用到了startActivityForResult方法，这个方法执行完后都会执行onActivityResult方法，
		 * 所以为了区别到底选择了那个方式获取图片要进行判断
		 * ，这里的requestCode跟startActivityForResult里面第二个参数对应
		 */

		if (requestCode == 0) {

			// 方式一
			/*
			 * try { //获得图片的uri Uri orginalUri = data.getData(); //将图片内容解析成字节数组
			 * mContent =
			 * readStream(contentResolver.openInputStream(Uri.parse(orginalUri
			 * .toString()))); //将字节数组转换为ImageView可调用的Bitmap对象 myBitmap
			 * =getPicFromBytes(mContent,null); ////把得到的图片绑定在控件上显示
			 * imageView.setImageBitmap(myBitmap); } catch (Exception e) {
			 * e.printStackTrace(); // TODO: handle exception }
			 */

			// 方式二
			try {
				Uri selectedImage = data.getData();
				String[] filePathColumn = { MediaStore.Images.Media.DATA };

				Cursor cursor = getActivity().getContentResolver().query(
						selectedImage, filePathColumn, null, null, null);
				cursor.moveToFirst();

				int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
				String picturePath = cursor.getString(columnIndex);
				cursor.close();
				imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

		} else if (requestCode == 1) {
			try {
				Bundle extras = data.getExtras();
				myBitmap = (Bitmap) extras.get("data");
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				mContent = baos.toByteArray();
			} catch (Exception e) {
				e.printStackTrace();
				// TODO: handle exception
			}
			imageView.setImageBitmap(myBitmap);
		}

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
}
