package edu.lakeland.mealrater;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import androidx.fragment.app.DialogFragment;


public class RateMealDialogueFragment extends DialogFragment {

    public interface SaveDateListener{
        void didFinishDatePickerDialog(Float rating);
    }

    public RateMealDialogueFragment()
    {

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        // Create the view from the the xml.
        final View view = inflater.inflate(R.layout.rate_meal, container);

        getDialog().setTitle(R.string.select);


        Button submitButton = view.findViewById(R.id.btnRatingSubmit);
        RatingBar rb = view.findViewById(R.id.rbMeal);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveItem(rb.getRating());
            }
        });

        //return super.onCreateView(inflater, container, savedInstanceState);
        return view;
    }

    private void saveItem(Float rating) {
        SaveDateListener activity = (SaveDateListener)getActivity();
        activity.didFinishDatePickerDialog(rating);
        getDialog().dismiss();
    }

}
