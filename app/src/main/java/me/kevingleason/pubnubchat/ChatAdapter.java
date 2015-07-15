package me.kevingleason.pubnubchat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import me.kevingleason.pubnubchat.adt.ChatMessage;

/**
 * Created by GleasonK on 7/11/15.
 *
 * Custom adapter that takes a list of ChatMessages and fills a chat_row_layout.xml view with the
 *   ChatMessage's information.
 */
public class ChatAdapter extends ArrayAdapter<ChatMessage> {
    private final Context context;
    private LayoutInflater inflater;
    private List<ChatMessage> values;
    private Set<String> onlineNow = new HashSet<String>();

    public ChatAdapter(Context context, List<ChatMessage> values) {
        super(context, R.layout.chat_row_layout, android.R.id.text1, values);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.values=values;
    }

    class ViewHolder {
        TextView user;
        TextView message;
        TextView timeStamp;
        View userPresence;
        ChatMessage chatMsg;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChatMessage chatMsg = this.values.get(position);
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.chat_row_layout, parent, false);
            holder.user = (TextView) convertView.findViewById(R.id.chat_user);
            holder.message = (TextView) convertView.findViewById(R.id.chat_message);
            holder.timeStamp = (TextView) convertView.findViewById(R.id.chat_time);
            holder.userPresence = convertView.findViewById(R.id.user_presence);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.user.setText(chatMsg.getUsername());
        holder.message.setText(chatMsg.getMessage());
        holder.timeStamp.setText(formatTimeStamp(chatMsg.getTimeStamp()));
        holder.chatMsg=chatMsg;
        holder.userPresence.setBackgroundDrawable( // If online show the green presence dot
                this.onlineNow.contains(chatMsg.getUsername())
                ? context.getResources().getDrawable(R.drawable.online_circle)
                : null);
        return convertView;
    }

    @Override
    public int getCount() {
        return this.values.size();
    }

    /**
     * Method to add a single message and update the listview.
     * @param chatMsg Message to be added
     */
    public void addMessage(ChatMessage chatMsg){
        this.values.add(chatMsg);
        notifyDataSetChanged();
    }

    /**
     * Method to add a list of messages and update the listview.
     * @param chatMsgs Messages to be added
     */
    public void setMessages(List<ChatMessage> chatMsgs){
        this.values.clear();
        this.values.addAll(chatMsgs);
        notifyDataSetChanged();
    }

    /**
     * Handle users. Fill the onlineNow set with current users. Data is used to display a green dot
     *   next to users who are currently online.
     * @param user UUID of the user online.
     * @param action The presence action
     */
    public void userPresence(String user, String action){
        boolean isOnline = action.equals("join") || action.equals("state-change");
        if (!isOnline && this.onlineNow.contains(user))
            this.onlineNow.remove(user);
        else if (isOnline && !this.onlineNow.contains(user))
            this.onlineNow.add(user);

        notifyDataSetChanged();
    }

    /**
     * Overwrite the onlineNow array with all the values attained from a call to hereNow().
     * @param onlineNow
     */
    public void setOnlineNow(Set<String> onlineNow){
        this.onlineNow = onlineNow;
        notifyDataSetChanged();
    }

    /**
     * Format the long System.currentTimeMillis() to a better looking timestamp. Uses a calendar
     *   object to format with the user's current time zone.
     * @param timeStamp
     * @return
     */
    public static String formatTimeStamp(long timeStamp){
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeStamp);
        return formatter.format(calendar.getTime());
    }

    /**
     * Clear all values from the values array and update the listview. Used when changing rooms.
     */
    public void clearMessages(){
        this.values.clear();
        notifyDataSetChanged();
    }

}
