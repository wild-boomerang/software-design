package by.bsuir.wildboom.lab2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class DeleteAllTimersDialog extends DialogFragment {
    private IDialogInteraction dialogInteraction;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        dialogInteraction = (IDialogInteraction) context;
    }

    @NonNull
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final String dialog = getArguments().getString("dialog");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(getResources().getString(R.string.confirmation));
        builder.setMessage(dialog);
        builder.setPositiveButton(
                getResources().getString(R.string.confirm),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialogInteraction.remove("");
                    }
                });
        builder.setNegativeButton(getResources().getString(R.string.cancel), null);
        return builder.create();
    }
}
