package com.vnptt.tms.api;

import com.vnptt.tms.api.output.table.UserOutput;
import com.vnptt.tms.dto.UserDTO;
import com.vnptt.tms.exception.ResourceNotFoundException;
import com.vnptt.tms.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("TMS/api")
public class UserApi {

    @Autowired
    private IUserService userService;

    /**
     * api Show User for web
     *
     * @param page
     * @param limit
     * @return
     */
    @GetMapping(value = "/user")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public UserOutput showUser(@RequestParam(value = "page", required = false) Integer page,
                               @RequestParam(value = "limit", required = false) Integer limit,
                               @RequestParam(value = "active", required = false) Integer active,
                               @RequestParam(value = "search", required = false) String string) {
        UserOutput result = new UserOutput();

        if (active != null && active != 1 && active != 0) {
            throw new ResourceNotFoundException("active must be 0 or 1");
        }

        if (page != null && limit != null) {
            if (active != null) {
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                if (string != null) {
                    result.setListResult((userService.findAllWithNameOrEmailOrUsernameOrCompany(pageable, active, string, string, string, string)));
                    result.setTotalPage((int) Math.ceil((double) userService.totalItemWithNameOrEmailOrUsernameOrCompany(active, string, string, string, string) / limit));
                } else {
                    result.setListResult((userService.findAllWithActive(pageable, active)));
                    result.setTotalPage((int) Math.ceil((double) userService.totalItemWithActive(active) / limit));
                }
            } else {
                result.setPage(page);
                Pageable pageable = PageRequest.of(page - 1, limit);
                if (string != null) {
                    result.setListResult((userService.findAllWithNameOrEmailOrUsernameOrCompany(pageable, 1, string, string, string, string)));
                    result.setTotalPage((int) Math.ceil((double) userService.totalItemWithNameOrEmailOrUsernameOrCompany(1, string, string, string, string) / limit));
                } else {
                    result.setListResult((userService.findAll(pageable)));
                    result.setTotalPage((int) Math.ceil((double) userService.totalItem() / limit));
                }
            }
        } else {
            if (active != null) {
                if (string != null) {
                    result.setListResult((userService.findAllWithNameOrEmailOrUsernameOrCompany(null, active, string, string, string, string)));
                } else {
                    result.setListResult(userService.findAllWithActive(active));
                }
            } else {
                if (string != null) {
                    result.setListResult((userService.findAllWithNameOrEmailOrUsernameOrCompany(null, 1, string, string, string, string)));
                } else {
                    result.setListResult(userService.findAll());
                }
            }
        }

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }


    /**
     * api find user by id
     *
     * @param id
     * @return
     */
    @GetMapping(value = "/user/{id}")
    public UserDTO showUser(@PathVariable("id") Long id) {
        return userService.findOne(id);
    }

    /**
     * api Show list User with rule for web
     *
     * @param ids
     * @return
     */
    @GetMapping(value = "/user/rule")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public UserOutput showUserWithRule(@RequestBody Long[] ids) {
        UserOutput result = new UserOutput();
        result.setListResult(userService.findAllWithRule(ids));

        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            throw new ResourceNotFoundException("no matching element found");
        }
        return result;
    }

    /**
     * api get list User management List device
     *
     * @return
     */
    @GetMapping(value = "/listDevice/{listDeviceId}/user")
    public UserOutput showListDeviceInRoles(@PathVariable(name = "listDeviceId") Long listDeviceId,
                                            @RequestParam(value = "page") Integer page,
                                            @RequestParam(value = "limit") Integer limit,
                                            @RequestParam(value = "search", required = false) String search) {
        UserOutput result = new UserOutput();
        result.setPage(page);
        Pageable pageable = PageRequest.of(page - 1, limit);
        if (search != null) {
            result.setListResult(userService.findUserManagementListDeviceWithName(listDeviceId, search, pageable));
            result.setTotalPage((int) Math.ceil((double) userService.totalUserManagementListDeviceWithName(listDeviceId, search) / limit));
        } else {
            result.setListResult(userService.findUserManagementListDevice(listDeviceId, pageable));
            result.setTotalPage((int) Math.ceil((double) userService.totalUserManagementListDevice(listDeviceId) / limit));
        }


        if (result.getListResult().size() >= 1) {
            result.setMessage("Request Success");
            result.setTotalElement(result.getListResult().size());
        } else {
            result.setMessage("no matching element found");
        }
        return result;
    }

    /**
     * api create new user (only use to test before token
     *
     * @param model
     * @return
     */

    /**
     * api update password
     *
     * @param id
     * @return
     */
    @PutMapping(value = "/user/password/{id}")
    public UserDTO updatePassword(@PathVariable("id") Long id,
                                  @RequestParam(name = "passwordold") String passwordold,
                                  @RequestParam(name = "passwordnew") String passwordnew) {
        return userService.updatePassword(id, passwordold, passwordnew);
    }

    /**
     * api update password
     *
     * @param id
     * @return
     */
    @PutMapping(value = "/user/admin/password/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public UserDTO forcedUpdatePassword(@PathVariable("id") Long id,
                                        @RequestParam(name = "passwordnew") String passwordnew) {
        return userService.forcedUpdatePassword(id, passwordnew);
    }

    /**
     * api update info user for admin
     *
     * @param model
     * @param id
     * @return
     */
    @PutMapping(value = "/user/admin/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public UserDTO forceUpdateUser(@RequestBody UserDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return userService.forceUpdate(model);
    }

    /**
     * api update info user for user
     *
     * @param model
     * @param id
     * @return
     */
    @PutMapping(value = "/user/{id}")
    public UserDTO updateUser(@RequestBody UserDTO model, @PathVariable("id") Long id) {
        model.setId(id);
        return userService.update(model);
    }

    /**
     * api change active to off
     *
     * @param id
     */
    @DeleteMapping(value = "/user/remove/{id}")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
    public void removeUser(@PathVariable("id") Long id) {
        userService.remove(id);
    }

    /**
     * api only use to test
     *
     * @param ids
     */
    @DeleteMapping(value = "/user")
    @PreAuthorize("hasRole('MODERATOR')")
    public void removeUser(@RequestBody Long[] ids) {
        userService.delete(ids);
    }
}
