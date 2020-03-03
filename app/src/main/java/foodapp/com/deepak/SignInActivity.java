package foodapp.com.deepak;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import foodapp.Common.Common;

public class SignInActivity extends AppCompatActivity {
    EditText edtPhone,edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        edtPhone=(EditText)findViewById(R.id.edtPhone);
        edtPassword=(EditText)findViewById(R.id.edtPassword);
        btnSignIn=(Button)findViewById(R.id.btnSignIn);
        final FirebaseDatabase database=FirebaseDatabase.getInstance();
        final DatabaseReference table_user=database.getReference("User");
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mDialog=new ProgressDialog(SignInActivity.this);
                mDialog.setMessage("Please Waiting...");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone(edtPhone.getText().toString());

                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                Intent homeintent=new Intent(SignInActivity.this,HomeActivity.class);
                                Common.currentUser=user;
                                startActivity(homeintent);
                                finish();
                                Toast.makeText(SignInActivity.this, "SignIn Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SignInActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            mDialog.dismiss();
                            Toast.makeText(SignInActivity.this, "user not exist", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
