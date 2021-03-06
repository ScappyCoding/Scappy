package com.codeofscappy.chat.view.profile;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.codeofscappy.chat.R;
import com.codeofscappy.chat.common.Common;
import com.codeofscappy.chat.databinding.ActivityProfileBinding;
import com.codeofscappy.chat.view.display.ViewImageActivity;
import com.codeofscappy.chat.view.settings.SettingsActivity;
import com.codeofscappy.chat.view.startup.SplashScreenActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.Objects;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private FirebaseUser firebaseUser;
    private FirebaseFirestore firestore;
    private ImageButton btnStatus;

    private BottomSheetDialog bottomSheetDialog, bsDialogEditName, bsDialogEditStatus;
    private ProgressDialog progressDialog;

    private int IMAGE_GALLERY_REQUEST = 111;
    private int CAMERA_PIC_REQUEST = 112;
    private Uri imageUri, cameraUri;

    public ProfileActivity() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_profile);


        // Implement Toolbar BackArrow get BackOption
        Toolbar toolbar = (Toolbar)  findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, SettingsActivity.class));
            }
        });


        // Instance of Firebase Database
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        firestore = FirebaseFirestore.getInstance();
        // Init ProgressDialog Window
        progressDialog = new ProgressDialog(this);

        // Pr??fung ob User in Database
        if (firebaseUser != null){
            getInfo();
        }

        // Initial BottomSheetPhoto
        initActionClick();

        // Initial BottomSheetName
        ClickAction();

        //Initial BottomSheetStatus
        initStatus();




    }

    // Button for BottomSheetDialog EditStatus
    private void initStatus() {
        binding.proEditStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomEditStatus();

            }
        });
    }

    // Button for  BottomSheetDialog EditName
    private void ClickAction() {
        binding.proEditName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetEditName();
            }
        });
    }

    // Button for BottomSheetDialog PhotoGallery
    private void initActionClick() {
        binding.fabCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomSheetPickPhoto();

            }
        });









        // Zoomable Image Methode
        binding.imageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.imageProfile.invalidate();
                Drawable dr = binding.imageProfile.getDrawable();
                Common.IMAGE_BITMAP = ((BitmapDrawable)dr.getCurrent()).getBitmap();
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfileActivity.this,
                        binding.imageProfile,"image");
                Intent intent = new Intent(ProfileActivity.this, ViewImageActivity.class);
                startActivity(intent, activityOptionsCompat.toBundle());
            }
        });

        // Logout
        binding.btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogSignOut();
            }
        });

    }


    // Methode for the DialogSheet
    @SuppressLint("ObsoleteSdkInt")
    private void showBottomSheetPickPhoto() {
          View view = getLayoutInflater().inflate(R.layout.bottom_sheet_pick,null);

        ((View) view.findViewById(R.id.ln_gallery)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();


            }
        });

        // test Camera
        ((View) view.findViewById(R.id.ln_camera)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera();
                Toast.makeText(ProfileActivity.this, "Camera Mode, Not possible yet", Toast.LENGTH_SHORT).show();
                bottomSheetDialog.dismiss();

            }
        });

        bsDialogEditName = new BottomSheetDialog(this);
        bsDialogEditName.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bsDialogEditName.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bsDialogEditName.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bsDialogEditName = null;
            }
        });

        bsDialogEditName.show();


    }

    @SuppressLint("ObsoleteSdkInt")
    private void showBottomSheetEditName() {
        View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_name,null);

        ((View) view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();
            }
        });

        // Takes the input from User from BottomSheetEdit  and Update the View
        EditText edUserName = view.findViewById(R.id.ed_username);
        ((View) view.findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateName(edUserName.getText().toString());
                if (TextUtils.isEmpty(edUserName.getText().toString())){
                    Toast.makeText(getApplicationContext(), "Name can??e be empty", Toast.LENGTH_SHORT).show();
                } else {
                    updateName(edUserName.getText().toString());
                    bottomSheetDialog.dismiss();
                }


            }
        });



        bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(view);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Objects.requireNonNull(bottomSheetDialog.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        bottomSheetDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                bottomSheetDialog = null;
            }
        });

        bottomSheetDialog.show();

    }


    // BottomSheetDialog UserStatus-Edit
    private void showBottomEditStatus() {

            @SuppressLint("InflateParams") View view = getLayoutInflater().inflate(R.layout.bottom_sheet_edit_status,null);

        ((View) view.findViewById(R.id.btn_cancel)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bottomSheetDialog.dismiss();


            }
        });
        EditText proEditStatus = view.findViewById(R.id.pro_EditStatus);
        ((View) view.findViewById(R.id.btn_save)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEditStatus(proEditStatus.getText().toString());
                bsDialogEditStatus.dismiss();

            }
        });

            bsDialogEditStatus = new BottomSheetDialog(this);
            bsDialogEditStatus.setContentView(view);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Objects.requireNonNull(bsDialogEditStatus.getWindow()).addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }

            bsDialogEditStatus.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    bsDialogEditStatus = null;
                }
            });

            bsDialogEditStatus.show();

        }


    // This Methode Read the Data from Current-User and Push this Into the View-Elements
    private void getInfo() {
        firestore.collection("Users").document(firebaseUser.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>()
        {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userName = documentSnapshot.get("userName").toString();
                String status = documentSnapshot.get("status").toString();
                String userPhone = documentSnapshot.get("userPhone").toString();
                String imageProfile = documentSnapshot.get("imageProfile").toString();


                binding.tvUsername.setText(userName);
                binding.tvPhone.setText(userPhone);
                binding.tvStatus.setText(status);
                // Download das Image via Url und f??gt es in ImageProfile View ein
                Glide.with(ProfileActivity.this).load(imageProfile).placeholder(R.drawable.avatar).into(binding.imageProfile);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }


    // Open Mobile-Pictures-Gallery
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"), IMAGE_GALLERY_REQUEST);
    }


    // Open Mobile Camera-Tool
    private void openCamera() {
        Intent cameraintent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraintent, CAMERA_PIC_REQUEST);
    }


   // Progress-Auth for pick a  Pictures in Mobile
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_GALLERY_REQUEST
        && resultCode == RESULT_OK
        && data != null
        && data.getData() != null)
        {

            imageUri = data.getData();

            uploadToFirebase();

          //  try {
          //      Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
          //      binding.imageProfile.setImageBitmap(bitmap);
//
          //  } catch (Exception e) {
          //      e.printStackTrace();
          //  }
        }

       // Testing Camera Pick Image
      if (requestCode == CAMERA_PIC_REQUEST
            && resultCode == RESULT_OK
            && data != null
            && data.getData() != null)
      {
          imageUri = data.getData();

          Bundle extras = data.getExtras();
          Bitmap image = (Bitmap) data.getExtras().get("data");
          CircularImageView circularImageView = (CircularImageView) findViewById(R.id.image_profile); // Set image as Bitmap
          circularImageView.setImageBitmap(image);
      }



    }


    // Makes a Uri from Image
    private String getFileExtention(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    // Upload (Gallery-Picture) or (Camera-Picture) to Firebase-Database
    private void uploadToFirebase() {
        if (imageUri != null) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            StorageReference riverRef = FirebaseStorage.getInstance().getReference().child("imageProfile/"+ System.currentTimeMillis()+ "," +getFileExtention(imageUri));
            riverRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                    while (!urlTask.isSuccessful());
                    Uri downloadUrl = urlTask.getResult();

                    final String sdownload_url = String.valueOf(downloadUrl);

                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("imageProfile", sdownload_url);

                    progressDialog.dismiss();
                    firestore.collection("Users").document(firebaseUser.getUid()).update(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                         @Override
                         public void onSuccess(Void aVoid) {
                             Toast.makeText(getApplicationContext(), "Upload Successfully", Toast.LENGTH_SHORT).show();


                             getInfo();
                         }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }


    // Update the new Username in Firestore-Database and make Toast when Success Saving..
    private void updateName(String newName) {
        firestore.collection("Users").document(firebaseUser.getUid()).update("userName",newName)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                        getInfo();
                    }
                });
    }


    // SignOut --> Methode whit Dialog-Windows
    private void showDialogSignOut() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setMessage("Do you want to sign out?");
        builder.setPositiveButton("Sign Out", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, SplashScreenActivity.class));
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    // Update Edit-Status to Firebase-Database
    private void updateEditStatus(String status) {
        firestore.collection("Users").document(firebaseUser.getUid()).update("status",status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(getApplicationContext(), "Update Successfully", Toast.LENGTH_SHORT).show();
                        getInfo();
                    }
                });
    }


}