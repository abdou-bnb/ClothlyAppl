package com.clothly.ecommerce.data.ui.userfeedback;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.clothly.ecommerce.data.R;
import com.clothly.ecommerce.data.data.helper.base.BasePresenter;
import com.clothly.ecommerce.data.data.helper.response.FeedBackResponse;
import com.clothly.ecommerce.data.data.provider.reposervices.ApiService;
import com.clothly.ecommerce.data.data.provider.retrofit.RetrofitClient;
import com.clothly.ecommerce.data.data.util.Constants;
import com.clothly.ecommerce.data.data.util.CustomSharedPrefs;
import com.clothly.ecommerce.data.data.util.NetworkHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.clothly.ecommerce.data.ui.userfeedback.FeedbackActivity.REQUEST_CAMERA;
import static com.clothly.ecommerce.data.ui.userfeedback.FeedbackActivity.SELECT_FILE;

public class FeedbackPresenter extends BasePresenter<FeedbackMvpView> {

    private Bitmap thumbnail;
    private Bitmap bm;
    private boolean flag;
    private String path;

    /**
     * Get review data
     * @param rating rating
     * @param imagePathList image Path List
     * @param review review
     * @param itemId product id
     * @param context  mActivity
     */
    public void getReviewData(String rating,List<String> imagePathList,String review,String itemId, Context context) {
        RequestBody requestBodyUserID = RequestBody.create(MediaType.parse(context.getString(R.string.partbody)), CustomSharedPrefs.getLoggedInUserId(context));
        RequestBody requestBodyComment  = RequestBody.create(MediaType.parse(context.getString(R.string.partbody)), review);
        RequestBody requestBodyRating = RequestBody.create(MediaType.parse(context.getString(R.string.partbody)), rating);
        RequestBody  requestBodyItemId = RequestBody.create(MediaType.parse(context.getString(R.string.partbody)), itemId);
        RequestBody apiToken = RequestBody.create(MediaType.parse(context.getString(R.string.partbody)), Constants.ServerUrl.API_TOKEN);

        MultipartBody.Part[] reviewImagesParts=null;
        if (imagePathList!=null){
            reviewImagesParts= new MultipartBody.Part[imagePathList.size()];
            for (int index = 0; index < imagePathList.size(); index++) {
                File file = new File(imagePathList.get(index));
                RequestBody surveyBody = RequestBody.create(MediaType.parse(context.getString(R.string.multipart)), file);
                reviewImagesParts[index] = MultipartBody.Part.createFormData("images[]", file.getName(), surveyBody);
            }
        }

        addReviewToServer(context, apiToken, requestBodyUserID, requestBodyItemId,reviewImagesParts,requestBodyRating, requestBodyComment);
    }

    /**
     * Add review
     * @param context mActivity
     * @param apiToken apiToken
     * @param userID userID
     * @param itemID itemID
     * @param rating rating
     * @param files files
     * @param review review
     */
    public void addReviewToServer(Context context, RequestBody apiToken, RequestBody userID, RequestBody itemID,
                          MultipartBody.Part[] files,RequestBody rating,RequestBody review){
        if (NetworkHelper.hasNetworkAccess(context)) {

            ApiService getRestInfoApi = RetrofitClient.getApiService();

            Call<FeedBackResponse> call = getRestInfoApi.addReviewToServer(userID,itemID,rating,review,apiToken,files);
            call.enqueue(new Callback<FeedBackResponse>() {
                @Override
                public void onResponse(@NonNull Call<FeedBackResponse> call, @NonNull Response<FeedBackResponse> response) {
                    if (response.body() != null) {
                        getMvpView().onGettingReviewSuccess(response.body());
                    }
                }

                @Override
                public void onFailure(@NonNull Call<FeedBackResponse> call, @NonNull Throwable t) {
                       getMvpView().onGettingReviewError(t.getMessage());//t.getMessage()
                }
            });
        }


    }

    /**
     * this methos opena  alert to choose image
     *
     * @param activity
     */
    public void selectImage(final Activity activity) {
        final CharSequence[] items = {activity.getResources().getString(R.string.take_photo),
                activity.getResources().getString(R.string.choose_from_gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(activity.getResources().getString(R.string.photo_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals(activity.getResources().getString(R.string.take_photo))) {
                    cameraIntent(activity);

                } else if (items[item].equals(activity.getResources().getString(R.string.choose_from_gallery))) {
                    galleryIntent(activity);

                }
            }
        });


        builder.show();
    }


    /**
     * choose image from gallery
     *
     * @param activity
     */
    private void galleryIntent(Activity activity) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        activity.startActivityForResult(Intent.createChooser(intent, activity.getResources()
                .getString(R.string.select_file)), SELECT_FILE);
    }

    /**
     * choose image from camera
     *
     * @param activity
     */
    private void cameraIntent(Activity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activity.startActivityForResult(intent, REQUEST_CAMERA);
    }

    /**
     * getting path from taking image on camera
     *
     * @param data
     * @param activity
     */
    public void onCaptureImageResult(Intent data, Activity activity) {
        if (data.getExtras() != null) {
            thumbnail = (Bitmap) data.getExtras().get("data");
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

            flag = true;
            Uri savedImageURI = Uri.parse("" + thumbnail);
            path = savedImageURI.toString();
            if (thumbnail == null) {
                Toast.makeText(activity, activity.getString(R.string.image_is_corrupted), Toast.LENGTH_SHORT).show();
            } else {
                String path = MediaStore.Images.Media.insertImage(activity.getContentResolver(), thumbnail, "Title", null);
                Uri fileUri = Uri.parse(path);
                String filePath = getRealPathFromURIPath(fileUri, activity);
                getMvpView().onGettingImagePath(filePath);
                getMvpView().onGettingBitmap(thumbnail);
                //make network call

            }
        }
    }

    /**
     * getting path from gallery image
     *
     * @param data
     * @param activity
     */
    public void onSelectFromGalleryResult(Intent data, Activity activity) {
        bm = null;
        if (data != null) {
            try {
                try {
                    bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());

                } catch (OutOfMemoryError e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream bytes = new ByteArrayOutputStream();

                if (bm == null) {
                    Toast.makeText(activity, activity.getString(R.string.image_is_corrupted), Toast.LENGTH_SHORT).show();
                } else {
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, bytes);

                    flag = true;
                    Uri savedImageURI = Uri.parse("" + bm);
                    Uri dataUri = data.getData();
                    path = savedImageURI.toString();
                    String filePath = getRealPathFromURIPath(dataUri, activity);
                    getMvpView().onGettingImagePath(filePath);
                    getMvpView().onGettingBitmap(bm);
                    //make network call
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * getting image path from uri
     *
     * @param contentURI
     * @param activity
     * @return : path
     */
    public String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            cursor.moveToFirst();
            String image_id = cursor.getString(0);
            image_id = image_id.substring(image_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getApplicationContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, MediaStore.Images.Media._ID + " = ? ", new String[]{image_id}, null);

        }
        if (cursor != null && cursor.moveToFirst()) {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        } else {

            return contentURI.getPath();
        }
    }


}
