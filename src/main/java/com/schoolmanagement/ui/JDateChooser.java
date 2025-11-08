package com.schoolmanagement.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class JDateChooser extends JPanel {
    private JTextField dateField;
    private JButton calendarButton;
    private JDialog calendarDialog;
    private CalendarPanel calendarPanel;
    private SimpleDateFormat dateFormat;
    private String dateFormatString = "yyyy-MM-dd";
    
    public JDateChooser() {
        initializeComponents();
        setupLayout();
        setupEventListeners();
    }
    
    private void initializeComponents() {
        dateFormat = new SimpleDateFormat(dateFormatString);
        
        dateField = new JTextField(10);
        dateField.setEditable(true);
        
        calendarButton = new JButton("📅");
        calendarButton.setPreferredSize(new Dimension(30, 30));
        calendarButton.setToolTipText("Select date");
        
        calendarPanel = new CalendarPanel();
        calendarDialog = new JDialog((Frame) null, "Select Date", true);
        calendarDialog.setContentPane(calendarPanel);
        calendarDialog.pack();
        calendarDialog.setLocationRelativeTo(null);
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        add(dateField, BorderLayout.CENTER);
        add(calendarButton, BorderLayout.EAST);
        
        setPreferredSize(new Dimension(200, 30));
        setMaximumSize(new Dimension(200, 30));
    }
    
    private void setupEventListeners() {
        calendarButton.addActionListener(e -> showCalendar());
        
        dateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateDateInput();
            }
        });
        
        dateField.addActionListener(e -> validateDateInput());
    }
    
    private void showCalendar() {
        calendarPanel.setSelectedDate(getDate());
        calendarDialog.setVisible(true);
        
        Date selectedDate = calendarPanel.getSelectedDate();
        if (selectedDate != null) {
            setDate(selectedDate);
        }
    }
    
    private void validateDateInput() {
        String text = dateField.getText().trim();
        if (text.isEmpty()) {
            return;
        }
        
        try {
            Date parsedDate = dateFormat.parse(text);
            setDate(parsedDate);
        } catch (Exception e) {
            // Invalid date format, show error or reset
            Toolkit.getDefaultToolkit().beep();
            dateField.setText("");
        }
    }
    
    public Date getDate() {
        String text = dateField.getText().trim();
        if (text.isEmpty()) {
            return null;
        }
        
        try {
            return dateFormat.parse(text);
        } catch (Exception e) {
            return null;
        }
    }
    
    public void setDate(Date date) {
        if (date == null) {
            dateField.setText("");
        } else {
            dateField.setText(dateFormat.format(date));
        }
    }
    
    public void setDateFormatString(String format) {
        this.dateFormatString = format;
        this.dateFormat = new SimpleDateFormat(format);
        
        // Update current date with new format
        Date currentDate = getDate();
        if (currentDate != null) {
            setDate(currentDate);
        }
    }
    
    public JTextField getDateEditor() {
        return dateField;
    }
    
    // Inner class for calendar panel
    private class CalendarPanel extends JPanel {
        private Calendar calendar;
        private JLabel monthYearLabel;
        private JPanel daysPanel;
        private Date selectedDate;
        private int currentMonth, currentYear;
        
        public CalendarPanel() {
            calendar = Calendar.getInstance();
            currentMonth = calendar.get(Calendar.MONTH);
            currentYear = calendar.get(Calendar.YEAR);
            selectedDate = new Date();
            
            initializeComponents();
            setupLayout();
            updateCalendar();
        }
        
        private void initializeComponents() {
            setLayout(new BorderLayout());
            setPreferredSize(new Dimension(280, 200));
            
            // Header panel with navigation
            JPanel headerPanel = new JPanel(new BorderLayout());
            
            JButton prevButton = new JButton("<");
            JButton nextButton = new JButton(">");
            monthYearLabel = new JLabel("", SwingConstants.CENTER);
            monthYearLabel.setFont(new Font("Arial", Font.BOLD, 14));
            
            prevButton.addActionListener(e -> {
                currentMonth--;
                if (currentMonth < 0) {
                    currentMonth = 11;
                    currentYear--;
                }
                updateCalendar();
            });
            
            nextButton.addActionListener(e -> {
                currentMonth++;
                if (currentMonth > 11) {
                    currentMonth = 0;
                    currentYear++;
                }
                updateCalendar();
            });
            
            headerPanel.add(prevButton, BorderLayout.WEST);
            headerPanel.add(monthYearLabel, BorderLayout.CENTER);
            headerPanel.add(nextButton, BorderLayout.EAST);
            
            // Days panel
            daysPanel = new JPanel(new GridLayout(0, 7, 2, 2));
            
            add(headerPanel, BorderLayout.NORTH);
            add(daysPanel, BorderLayout.CENTER);
            
            // OK button
            JButton okButton = new JButton("OK");
            okButton.addActionListener(e -> {
                if (selectedDate != null) {
                    Window window = SwingUtilities.getWindowAncestor(this);
                    window.dispose();
                }
            });
            
            add(okButton, BorderLayout.SOUTH);
        }
        
        private void setupLayout() {
            // Add day names
            String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String dayName : dayNames) {
                JLabel label = new JLabel(dayName, SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 12));
                daysPanel.add(label);
            }
        }
        
        private void updateCalendar() {
            daysPanel.removeAll();
            
            // Add day names again
            String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
            for (String dayName : dayNames) {
                JLabel label = new JLabel(dayName, SwingConstants.CENTER);
                label.setFont(new Font("Arial", Font.BOLD, 12));
                daysPanel.add(label);
            }
            
            // Update month/year label
            String[] months = {"January", "February", "March", "April", "May", "June",
                             "July", "August", "September", "October", "November", "December"};
            monthYearLabel.setText(months[currentMonth] + " " + currentYear);
            
            // Set calendar to first day of month
            calendar.set(currentYear, currentMonth, 1);
            int firstDay = calendar.get(Calendar.DAY_OF_WEEK) - 1;
            int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
            
            // Add empty cells for days before month starts
            for (int i = 0; i < firstDay; i++) {
                daysPanel.add(new JLabel());
            }
            
            // Add day buttons
            Calendar today = Calendar.getInstance();
            for (int day = 1; day <= daysInMonth; day++) {
                JButton dayButton = new JButton(String.valueOf(day));
                dayButton.setMargin(new Insets(0, 0, 0, 0));
                dayButton.setFont(new Font("Arial", Font.PLAIN, 11));
                
                // Highlight today
                if (currentYear == today.get(Calendar.YEAR) && 
                    currentMonth == today.get(Calendar.MONTH) && 
                    day == today.get(Calendar.DAY_OF_MONTH)) {
                    dayButton.setBackground(Color.BLUE);
                    dayButton.setForeground(Color.WHITE);
                }
                
                final int selectedDay = day;
                dayButton.addActionListener(e -> {
                    Calendar selectedCal = Calendar.getInstance();
                    selectedCal.set(currentYear, currentMonth, selectedDay);
                    selectedDate = selectedCal.getTime();
                    
                    // Update all buttons
                    Component[] components = daysPanel.getComponents();
                    for (Component comp : components) {
                        if (comp instanceof JButton) {
                            JButton btn = (JButton) comp;
                            if (!btn.getText().isEmpty()) {
                                btn.setBackground(null);
                                btn.setForeground(Color.BLACK);
                            }
                        }
                    }
                    
                    // Highlight selected
                    dayButton.setBackground(Color.GREEN);
                    dayButton.setForeground(Color.WHITE);
                });
                
                daysPanel.add(dayButton);
            }
            
            // Fill remaining cells
            int remainingCells = 42 - (firstDay + daysInMonth);
            for (int i = 0; i < remainingCells; i++) {
                daysPanel.add(new JLabel());
            }
            
            daysPanel.revalidate();
            daysPanel.repaint();
        }
        
        public Date getSelectedDate() {
            return selectedDate;
        }
        
        public void setSelectedDate(Date date) {
            if (date != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);
                currentMonth = cal.get(Calendar.MONTH);
                currentYear = cal.get(Calendar.YEAR);
                selectedDate = date;
                updateCalendar();
            }
        }
    }
}