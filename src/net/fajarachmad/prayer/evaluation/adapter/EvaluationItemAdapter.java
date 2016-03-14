package net.fajarachmad.prayer.evaluation.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;

import com.google.gson.Gson;

import net.fajarachmad.prayer.R;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationDetailFragment;
import net.fajarachmad.prayer.evaluation.fragment.EvaluationFragment;
import net.fajarachmad.prayer.evaluation.wrapper.EvaluationItemWrapper;

import java.util.List;

/**
 * Created by user on 3/3/2016.
 */
public class EvaluationItemAdapter extends RecyclerView.Adapter<EvaluationItemAdapter.EvaluationItemViewHolder>{

    private List<EvaluationItemWrapper> evaluationItems;
    private EvaluationFragment context;
    private Gson gson;
    private int selectedPosition;

    public EvaluationItemAdapter(EvaluationFragment context, List<EvaluationItemWrapper> evaluationItems) {
        this.evaluationItems = evaluationItems;
        this.context = context;
        gson = new Gson();
    }

    @Override
    public int getItemCount() {
        return evaluationItems.size();
    }

    @Override
    public void onBindViewHolder(final EvaluationItemViewHolder evaluationItemViewHolder, final int i) {
        final EvaluationItemWrapper evaluationItem = evaluationItems.get(i);

        evaluationItemViewHolder.goalName.setText(evaluationItem.getGoalName());
        evaluationItemViewHolder.progress.setText(evaluationItem.getProgressString());
        evaluationItemViewHolder.dueDate.setText(evaluationItem.getDueDateString());

        if ((i+1) != evaluationItems.size()) {
            evaluationItemViewHolder.space.setVisibility(View.GONE);
        }

        evaluationItemViewHolder.cardView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String selectedItemStr = gson.toJson(evaluationItem);
                Bundle bundle = new Bundle();
                bundle.putString(EvaluationItemWrapper.class.getName(), selectedItemStr);
                Fragment fragment = new EvaluationDetailFragment();
                fragment.setArguments(bundle);
                context.getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container, fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        evaluationItemViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setSelectedPosition(i);
                return view.showContextMenu();
            }
        });
    }


    public int getSelectedPosition() {
        return selectedPosition;
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    @Override
    public EvaluationItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.evaluation_item_cardview, viewGroup, false);

        return new EvaluationItemViewHolder(itemView);
    }

    public static class EvaluationItemViewHolder extends RecyclerView.ViewHolder{

        protected TextView goalName;
        protected TextView progress;
        protected TextView dueDate;
        protected CardView cardView;
        protected Space space;

        public EvaluationItemViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.evaluation_item_cv);
            goalName = (TextView) itemView.findViewById(R.id.evaluation_item_goalname);
            progress = (TextView) itemView.findViewById(R.id.evaluation_item_progressinfo);
            dueDate = (TextView) itemView.findViewById(R.id.evaluation_item_duedate);
            space = (Space) itemView.findViewById(R.id.evaluation_item_spacebottom);
        }


    }
    }