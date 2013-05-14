package util;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.util.Log;

/**
 * 处理图片的工具类.
 * 
 */
public class ImageTools {

	// 黑白效果
	public static Bitmap toGrayscale(Bitmap bmpOriginal) {
		int width, height;
		height = bmpOriginal.getHeight();
		width = bmpOriginal.getWidth();

		Bitmap bmpGrayscale = Bitmap.createBitmap(width, height,
				Bitmap.Config.RGB_565);
		Canvas c = new Canvas(bmpGrayscale);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpOriginal, 0, 0, paint);
		return bmpGrayscale;
	}

	// 浮雕效果
	public static Bitmap toFuDiao(Bitmap mBitmap) {
		Paint mPaint;

		// Bitmap mBitmap;
		// Bitmap mBitmap = toGrayscale(bmpOriginal);
		int mBitmapWidth = 0;
		int mBitmapHeight = 0;

		int mArrayColor[] = null;
		int mArrayColorLengh = 0;
		long startTime = 0;
		int mBackVolume = 0;

		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();
		Log.d("zlc", "2");
		// Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
		// Bitmap.Config.ARGB_8888);
		Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
				Bitmap.Config.RGB_565);
		Log.d("zlc", "3");
		mArrayColorLengh = mBitmapWidth * mBitmapHeight;
		Log.d("zlc", "4");
		// mArrayColor = new int[mArrayColorLengh];
		// int[] mArrayColor2 = new int[mArrayColorLengh];
		// mBitmap.getPixels(mArrayColor2, 0, mBitmapWidth, 0, 0, mBitmapWidth,
		// mBitmapHeight);
		Log.d("zlc", "5");
		int count = 0;
		int preColor = 0;
		int prepreColor = 0;
		int color = 0;
		preColor = mBitmap.getPixel(0, 0);
		// preColor = mArrayColor2[0];
		Log.d("zlc", "4");

		for (int i = 0; i < mBitmapWidth; i++) {
			for (int j = 0; j < mBitmapHeight; j++) {
				int curr_color = mBitmap.getPixel(i, j);
				int r = Color.red(curr_color) - Color.red(prepreColor) + 128;
				int g = Color.green(curr_color) - Color.red(prepreColor) + 128;
				int b = Color.green(curr_color) - Color.blue(prepreColor) + 128;
				int a = Color.alpha(curr_color);
				int modif_color = Color.argb(a, r, g, b);
				bmpReturn.setPixel(i, j, modif_color);
				prepreColor = preColor;
				preColor = curr_color;
			}
		}

		// for (int i = 0; i < mArrayColorLengh; i++) {
		// color = mArrayColor2[i];
		// int r = Color.red(color) - Color.red(prepreColor) +128;
		// int g = Color.green(color) - Color.red(prepreColor) +128;
		// int b = Color.green(color) - Color.blue(prepreColor) +128;
		// int a = Color.alpha(color);
		//
		// mArrayColor[i] = Color.argb(a, r, g, b);
		// prepreColor = preColor;
		// preColor = color;
		//
		// }

		Log.d("zlc", "6");
		// bmpReturn.setPixels(mArrayColor, 0, mBitmapWidth, 0, 0, mBitmapWidth,
		// mBitmapHeight);
		Canvas c = new Canvas(bmpReturn);
		Paint paint = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
		paint.setColorFilter(f);
		c.drawBitmap(bmpReturn, 0, 0, paint);
		Log.d("zlc", "7");
		return bmpReturn;
	}

	/* 设置图片模糊 */
	public static Bitmap toMohu(Bitmap bmpSource, int Blur) // 源位图，模糊强度
	{
		Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(),
				bmpSource.getHeight(), Bitmap.Config.ARGB_8888);
		int pixels[] = new int[bmpSource.getWidth() * bmpSource.getHeight()];
		int pixelsRawSource[] = new int[bmpSource.getWidth()
				* bmpSource.getHeight() * 3];
		int pixelsRawNew[] = new int[bmpSource.getWidth()
				* bmpSource.getHeight() * 3];

		bmpSource.getPixels(pixels, 0, bmpSource.getWidth(), 0, 0,
				bmpSource.getWidth(), bmpSource.getHeight());

		for (int k = 1; k <= Blur; k++) {
			// 从图片中获取每个像素三原色的值
			for (int i = 0; i < pixels.length; i++) {
				pixelsRawSource[i * 3 + 0] = Color.red(pixels[i]);
				pixelsRawSource[i * 3 + 1] = Color.green(pixels[i]);
				pixelsRawSource[i * 3 + 2] = Color.blue(pixels[i]);
			}
			// 取每个点上下左右点的平均值作自己的值
			int CurrentPixel = bmpSource.getWidth() * 3 + 3;
			// 当前处理的像素点，从点(2,2)开始
			for (int i = 0; i < bmpSource.getHeight() - 3; i++) // 高度循环
			{
				for (int j = 0; j < bmpSource.getWidth() * 3; j++) // 宽度循环
				{
					CurrentPixel += 1; // 取上下左右，取平均值
					int sumColor = 0; // 颜色和
					sumColor = pixelsRawSource[CurrentPixel
							- bmpSource.getWidth() * 3]; // 上一点
					sumColor = sumColor + pixelsRawSource[CurrentPixel - 3]; // 左一点
					sumColor = sumColor + pixelsRawSource[CurrentPixel + 3]; // 右一点
					sumColor = sumColor
							+ pixelsRawSource[CurrentPixel
									+ bmpSource.getWidth() * 3]; // 下一点
					pixelsRawNew[CurrentPixel] = Math.round(sumColor / 4); // 设置像素点
				}
			}

			for (int i = 0; i < pixels.length; i++) {
				pixels[i] = Color.rgb(pixelsRawNew[i * 3 + 0],
						pixelsRawNew[i * 3 + 1], pixelsRawNew[i * 3 + 2]);
			}
		}

		bmpReturn.setPixels(pixels, 0, bmpSource.getWidth(), 0, 0,
				bmpSource.getWidth(), bmpSource.getHeight()); // 必须新建位图然后填充，不能直接填充源图像，否则内存报错
		return bmpReturn;
	}

	/* 设置图片积木效果 */
	public static Bitmap toJiMu(Bitmap mBitmap) {

		int mBitmapWidth = 0;
		int mBitmapHeight = 0;


		mBitmapWidth = mBitmap.getWidth();
		mBitmapHeight = mBitmap.getHeight();
		Bitmap bmpReturn = Bitmap.createBitmap(mBitmapWidth, mBitmapHeight,
				Bitmap.Config.ARGB_8888);

		int iPixel = 0;
		for (int i = 0; i < mBitmapWidth; i++) {
			for (int j = 0; j < mBitmapHeight; j++) {
				int curr_color = mBitmap.getPixel(i, j);

				int avg = (Color.red(curr_color) + Color.green(curr_color) + Color
						.blue(curr_color)) / 3;
				if (avg >= 100) {
					iPixel = 255;
				} else {
					iPixel = 0;
				}
				int modif_color = Color.argb(255, iPixel, iPixel, iPixel);

				bmpReturn.setPixel(i, j, modif_color);
			}
		}

		return bmpReturn;
	}

	/* 设置油画 */
	public static Bitmap toYouHua(Bitmap bmpSource) // 源位图
	{
		Bitmap bmpReturn = Bitmap.createBitmap(bmpSource.getWidth(),
				bmpSource.getHeight(), Bitmap.Config.ARGB_8888);
		int color = 0;
		int Radio = 0;
		int width = bmpSource.getWidth();
		int height = bmpSource.getHeight();

		Random rnd = new Random();
		int iModel = 10;
		int i = width - iModel;
		while (i > 1) {
			int j = height - iModel;
			while (j > 1) {
				int iPos = rnd.nextInt(100000) % iModel;
				// 将半径之内取任一点
				color = bmpSource.getPixel(i + iPos, j + iPos);
				bmpReturn.setPixel(i, j, color);
				j = j - 1;
			}
			i = i - 1;
		}
		return bmpReturn;
	}
}