package com.art.controllers;

import org.springframework.stereotype.Controller;

@Controller
public class MailingGroupsController {

//    @Resource(name = "mailingGroupsService")
//    private MailingGroupsService mailingGroupsService;
//
//    @Resource(name = "userService")
//    private UserService userService;
//
//    @Resource(name = "stuffService")
//    private StuffService stuffService;
//
//    @GetMapping(value = "/mailinggroups")
//    public String mailingGroupsPage(ModelMap model) {
//
//        List<MailingGroups> mailingGroupsList = mailingGroupsService.findAllWithUsers();
//        model.addAttribute("mailGroups", mailingGroupsList);
//        MailingGroups mailingGroups = new MailingGroups();
//        model.addAttribute("mailingGroups", mailingGroups);
//        return "mailingGroups";
//    }
//
//    @PostMapping(value = {"/newmailgroup"}, produces = "application/json;charset=UTF-8")
//    public @ResponseBody
//    GenericResponse saveGroup(@RequestBody MailingGroups mailingGroups) {
//        GenericResponse response = new GenericResponse();
//
//        List<BigInteger> idList = new ArrayList<>(0);
//
//        mailingGroups.getUsers().forEach(u -> idList.add(u.getId()));
//
//        List<Users> users = userService.findByIdInWithMailingGroups(idList);
//        mailingGroups = mailingGroupsService.create(mailingGroups);
//        Set<MailingGroups> mailingGroupsList = new HashSet<>(0);
//        mailingGroupsList.add(mailingGroups);
//
//        users.forEach(u -> mailingGroupsList.addAll(u.getMailingGroups()));
//        users.forEach(u -> u.setMailingGroups(mailingGroupsList));
//
//        userService.updateList(users);
//
//        response.setMessage("Группа " + mailingGroups.getMailingGroup() + " успешно добавлена.");
//
//        return response;
//    }
//
//    @RequestMapping(value = {"/edit-groups-{id}"}, method = RequestMethod.GET)
//    public String editGroup(@PathVariable BigInteger id, ModelMap model) {
//        String title = "Обновление данных группы рассылки";
//        MailingGroups mailingGroups = mailingGroupsService.findByIdWithUsers(id);
//
//        model.addAttribute("mailingGroups", mailingGroups);
//        model.addAttribute("edit", true);
//        model.addAttribute("title", title);
//        return "editMailingGroups";
//    }
//
//    @RequestMapping(value = {"/edit-groups-{id}"}, method = RequestMethod.POST)
//    public String updateGroup(@ModelAttribute("mailingGroups") MailingGroups mailingGroups, BindingResult result, ModelMap model) {
//        String ret = "списку групп рассылки.";
//        String redirectUrl = "/mailinggroups";
//        Set<MailingGroups> mg = new HashSet<>(0);
//        if (result.hasErrors()) {
//            return "editMailingGroup";
//        }
//
//        MailingGroups groups = mailingGroupsService.findByIdWithAllFields(mailingGroups.getId());
//
//        List<Users> usersList = groups.getUsers();
//
//        if (usersList.size() > mailingGroups.getUsers().size()) {
//            usersList.forEach(ul -> {
//                if (!mailingGroups.getUsers().contains(ul)) {
//                    ul.setMailingGroups(null);
//                }
//            });
//        } else if (usersList.size() < mailingGroups.getUsers().size()) {
//            mailingGroups.getUsers().forEach(users -> {
//                if (!usersList.contains(users)) {
//                    usersList.add(users);
//                }
//            });
//            usersList.forEach(u -> {
//                mg.add(mailingGroups);
//                u.setMailingGroups(mg);
//                u.setPassword(null);
//            });
//        }
//
//        mailingGroupsService.update(mailingGroups);
//
//        model.addAttribute("success", "Данные группы рассылки " +
//                mailingGroups.getMailingGroup() + " успешно обновлены.");
//
//        model.addAttribute("redirectUrl", redirectUrl);
//        model.addAttribute("ret", ret);
//        return "registrationsuccess";
//    }
//
//    @RequestMapping(value = {"/delete-groups-{id}"}, method = RequestMethod.GET)
//    public String deleteGroup(@PathVariable BigInteger id) {
//        List<Users> usersList = userService.findByMailingGroups(mailingGroupsService.findById(id));
//        usersList.forEach(u -> u.setMailingGroups(null));
//        mailingGroupsService.deleteById(id);
//        userService.updateList(usersList);
//        return "redirect:/mailinggroups";
//    }
//
//    @ModelAttribute("investors")
//    public List<Users> initializeInvestors() {
//        BigInteger stuffId = stuffService.findByStuff("Инвестор").getId();
//        return userService.findRentors(stuffId);
//    }

}
