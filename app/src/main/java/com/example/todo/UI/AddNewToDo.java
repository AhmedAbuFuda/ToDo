package com.example.todo.UI;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.todo.Model.ToDoModel;
import com.example.todo.R;
import com.example.todo.Utils.DatabaseHandler;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AddNewToDo extends BottomSheetDialogFragment {
    public static final String TAG = "ActionBottomDialog";

    private EditText newToDo;
    private Button saveButton;
    private DatabaseHandler db;

    public static AddNewToDo newInstance(){
        return new AddNewToDo();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style.DialogStyle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.new_todo, container, false);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        newToDo = requireView().findViewById(R.id.newToDoText);
        saveButton = getView().findViewById(R.id.newToDoButton);

        boolean isUpdate = false;

        final Bundle bundle = getArguments();
        if(bundle != null){
            isUpdate = true;
            String task = bundle.getString("task");
            newToDo.setText(task);
            if(task != null)
                saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
        }
        db = new DatabaseHandler(getActivity());
        db.openDatabase();

        newToDo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().equals("")){
                    saveButton.setEnabled(false);
                    saveButton.setTextColor(Color.GRAY);
                }
                else{
                    saveButton.setEnabled(true);
                    saveButton.setTextColor(ContextCompat.getColor(requireContext(), R.color.purple_500));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        final boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = newToDo.getText().toString();
                if(finalIsUpdate){
                    db.updateToD(bundle.getInt("id"), text);
                }
                else {
                    ToDoModel task = new ToDoModel();
                    task.setTodo(text);
                    task.setStatus(0);
                    db.insertToDo(task);
                }
                dismiss();
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog){
        Activity activity = getActivity();
        if(activity instanceof DialogCloseListener)
            ((DialogCloseListener)activity).handleDialogClose(dialog);
    }
}
